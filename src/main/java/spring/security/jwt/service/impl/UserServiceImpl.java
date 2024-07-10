package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.model.dto.user.RegistrationUserDto;
import spring.security.jwt.model.dto.user.UserDto;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.mapper.UserMapper;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.service.UserService;
import spring.security.jwt.utils.Encryptor;
import spring.security.jwt.utils.PasswordUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IamResponse<UserDto> saveNewUser(@NotNull RegistrationUserDto registrationUserDto) {
        // Password matching check
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return IamResponse.createFailed(ApiErrorMessage.MISMATCH_PASSWORDS.getMessage());
        }

        // Password validity check
        if (PasswordUtils.isNotValidPassword(registrationUserDto.getPassword())) {
            return IamResponse.createFailed(ApiErrorMessage.INVALID_PASSWORD.getMessage());
        }

        if (userRepository.findByUsername(registrationUserDto.getUsername()).isPresent()) {
            return IamResponse.createFailed(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(registrationUserDto.getUsername()));
        }

        if (userRepository.existsByEmail(registrationUserDto.getEmail())) {
            return IamResponse.createFailed(ApiErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage(registrationUserDto.getEmail()));
        }

        User newUser = new User();
        newUser.setUsername(registrationUserDto.getUsername());
        newUser.setEmail(registrationUserDto.getEmail());
        newUser.setCreated(LocalDateTime.now());
        newUser.setPassword(Encryptor.sha(registrationUserDto.getPassword()));
        newUser.setRegistrationStatus(RegistrationStatus.ACTIVE);
        newUser.setRoles(new HashSet<>());
        userRepository.save(newUser);

        // Переводим пользователя в DTO для ответа
        UserDto userDto = userMapper.toDto(newUser);
        return IamResponse.createSuccessful(userDto);
    }

    @Override
    @Transactional(readOnly = true)
    public IamResponse<UserDto> getById(@NotNull Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserDto userDto = userMapper.toDto(userOptional.get());
            return IamResponse.createSuccessful(userDto);
        } else {
            return IamResponse.createFailed(ApiErrorMessage.NOT_FOUND_USER_ID.getMessage());
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ApiErrorMessage.NOT_FOUND_USER_NAME.getMessage()));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public IamResponse<UserDto> getUserInfo(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    UserDto userDto = userMapper.toDto(user);
                    return IamResponse.createSuccessful(userDto);
                })
                .orElse(IamResponse.createFailed(ApiErrorMessage.NOT_FOUND_USER_ID.getMessage(username)));
    }

    @Transactional(readOnly = true)
    public IamResponse<LinkedList<UserDto>> findAllUsers() {
        List<User> users = userRepository.findAll(Sort.by("id"));
        LinkedList<UserDto> userDto = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return IamResponse.createSuccessful(userDto);
    }

}
