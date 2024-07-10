package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.mapper.UserMapper;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.model.dto.user.LoginRequest;
import spring.security.jwt.model.entities.RefreshToken;
import spring.security.jwt.model.entities.Role;
import spring.security.jwt.model.exception.InvalidDataException;
import spring.security.jwt.model.response.UserProfileDto;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.repositories.RoleRepository;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.service.AuthService;
import spring.security.jwt.security.JwtTokenUtils;
import spring.security.jwt.service.RefreshTokenService;
import spring.security.jwt.utils.ApiUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

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

        User user = userRepository.findUserByUsername(authRequest.getUsername())
                .orElseThrow(
                        () -> new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage()));

        if (Objects.equals(RegistrationStatus.PENDING_CONFIRMATION, user.getRegistrationStatus())) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_USER_REGISTRATION_STATUS.getMessage(user.getRegistrationStatus().name()));
        }

        RefreshToken refreshToken = refreshTokenService.refreshToken(user);

        String token = jwtTokenUtils.generateToken(user);
        UserProfileDto userProfileDto = userMapper.toUserProfileDto(user, token, refreshToken.getToken());
        userProfileDto.setToken(token);

        return IamResponse.createSuccessfulWithNewToken(userProfileDto);
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
