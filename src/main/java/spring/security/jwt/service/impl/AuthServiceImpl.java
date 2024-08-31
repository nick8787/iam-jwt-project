package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.mapper.UserMapper;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.exception.NotFoundException;
import spring.security.jwt.model.request.user.LoginRequest;
import spring.security.jwt.model.entities.RefreshToken;
import spring.security.jwt.model.entities.Role;
import spring.security.jwt.model.exception.InvalidDataException;
import spring.security.jwt.model.response.UserProfileDto;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.repositories.RoleRepository;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.security.validation.AccessValidator;
import spring.security.jwt.service.AuthService;
import spring.security.jwt.security.JwtTokenUtils;
import spring.security.jwt.service.RefreshTokenService;
import spring.security.jwt.service.model.IamServiceUserRole;
import spring.security.jwt.utils.ApiUtils;
import spring.security.jwt.utils.PasswordUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final AccessValidator accessValidator;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public IamResponse<UserProfileDto> login(@NotNull LoginRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.error(ApiErrorMessage.AUTHENTICATION_FAILED_FOR_USER.getMessage(), e);
            throw e;
        }

        User user = userRepository.findUserByUsernameAndDeletedFalse(authRequest.getUsername())
                .orElseThrow(
                        () -> new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage()));

        RefreshToken refreshToken = refreshTokenService.refreshToken(user);

        String token = jwtTokenUtils.generateToken(user);
        UserProfileDto userProfileDto = userMapper.toUserProfileDto(user, token, refreshToken.getToken());
        userProfileDto.setToken(token);

        return IamResponse.createSuccessfulWithNewToken(userProfileDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IamResponse<UserProfileDto> registerUser(@NotNull RegistrationUserRequest registrationUserRequest) {
        log.trace("Registering new user: {}", registrationUserRequest.getUsername());

        accessValidator.validateNewUserRegistration(registrationUserRequest);

        // Assign ROLE_USER by default
        Role userRole = roleRepository.findByName(IamServiceUserRole.USER.getRole())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.UNDEFINED_USER_ROLE.getMessage()));

        if (PasswordUtils.isNotValidPassword(registrationUserRequest.getPassword())) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_PASSWORD.getMessage());
        }

        User newUser = userMapper.fromDto(registrationUserRequest);
        newUser.setPassword(passwordEncoder.encode(registrationUserRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole); // Adding USER role
        newUser.setRoles(roles);
        userRepository.save(newUser);

        RefreshToken refreshToken = refreshTokenService.refreshToken(newUser);

        String token = jwtTokenUtils.generateToken(newUser);
        UserProfileDto userProfileDto = userMapper.toUserProfileDto(newUser, token, refreshToken.getToken());
        userProfileDto.setToken(token);

        return IamResponse.createSuccessful(userProfileDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public IamResponse<UserProfileDto> refreshTokens(String refreshTokenValue) {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        RefreshToken refreshToken = refreshTokenService.refreshToken(refreshTokenValue);
        User user = refreshToken.getUser();
        List<Role> userRoles = roleRepository.findActiveRolesByUserIdAndActiveIsTrue(user.getId());

        String token = jwtTokenUtils.generateToken(user);
        UserProfileDto profileDto = userMapper.map(user, userRoles, token, refreshToken.getToken());
        return IamResponse.createSuccessfulWithNewToken(profileDto);
    }

}
