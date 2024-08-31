package spring.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.jwt.kafka.MessageProducer;
import spring.security.jwt.kafka.service.KafkaMessageService;
import spring.security.jwt.mapper.UserMapper;
import spring.security.jwt.model.IamResponse;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.exception.InvalidDataException;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.dto.user.UserDTO;
import spring.security.jwt.model.entities.Role;
import spring.security.jwt.model.entities.User;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.model.exception.NotFoundException;
import spring.security.jwt.model.request.user.UpdateUserRequest;
import spring.security.jwt.repositories.CommentRepository;
import spring.security.jwt.repositories.PostRepository;
import spring.security.jwt.repositories.RoleRepository;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.security.validation.AccessValidator;
import spring.security.jwt.service.UserService;
import spring.security.jwt.service.model.IamServiceUserRole;
import spring.security.jwt.utils.PasswordUtils;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static spring.security.jwt.service.impl.PostServiceImpl.getUserDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccessValidator accessValidator;
    private final MessageProducer messageProducer;
    private final KafkaMessageService kafkaMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IamResponse<UserDTO> saveNewUser(@NotNull RegistrationUserRequest registrationUserRequest, String creatorUsername) {
        accessValidator.validateNewUserData(
                registrationUserRequest.getUsername(),
                registrationUserRequest.getEmail(),
                registrationUserRequest.getPassword(),
                registrationUserRequest.getConfirmPassword()
        );

        if (PasswordUtils.isNotValidPassword(registrationUserRequest.getPassword())) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_PASSWORD.getMessage());
        }

        Role userRole = roleRepository.findByName(IamServiceUserRole.USER.getRole())
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_ROLE.getMessage()));

        User newUser = userMapper.fromDto(registrationUserRequest);
        newUser.setCreatedBy(creatorUsername);
        newUser.setPassword(passwordEncoder.encode(registrationUserRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        newUser.setRoles(roles);
        userRepository.save(newUser);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendUserCreatedMessage(newUser.getId(), newUser.getUsername(), creatorUsername);

        UserDTO userDto = userMapper.toDto(newUser);
        return IamResponse.createSuccessful(userDto);
    }

    @Override
    @Transactional(readOnly = true)
    public IamResponse<UserDTO> getById(@NotNull Integer userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_ID.getMessage(userId)));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails currentUser = loadUserByUsername(username);

        accessValidator.validateAdminOrOwnerAccess(currentUser, user.getUsername(), user.getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_VIEW_USER.getMessage(userId));

        UserDTO userDto = userMapper.toDto(user);
        return IamResponse.createSuccessful(userDto);
    }

    @Override
    @Transactional
    public IamResponse<UserDTO> updateUser(@NotNull Integer userId, @NotNull UpdateUserRequest userRequest, String username) {
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_ID.getMessage(userId)));

        UserDetails currentUser = loadUserByUsername(username);

        accessValidator.validateAdminOrOwnerAccess(currentUser, user.getUsername(), user.getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_UPDATE.getMessage(userId));
        accessValidator.validateUniqueFields(userId, userRequest.getUsername(), userRequest.getEmail(), userRequest.getPhoneNumber());

        userMapper.update(user, userRequest);
        userRepository.save(user);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendUserUpdatedMessage(user.getId(), user.getUsername());

        UserDTO userDto = userMapper.toDto(user);
        return IamResponse.createSuccessful(userDto);
    }

    @Override
    @Transactional
    public void softDelete(Integer userId, String username) {
        User user = userRepository.findByIdAndCreatedBy(userId, username).orElseThrow(
                () -> new NotFoundException(ApiErrorMessage.NOT_FOUND_USER_ID_AND_CREATED_BY.getMessage(userId, username))
        );

        UserDetails currentUser = loadUserByUsername(username);

        accessValidator.validateAdminOrOwnerAccess(currentUser, user.getUsername(), user.getCreatedBy(), username,
                ApiErrorMessage.HAVE_NO_ACCESS_TO_DELETE.getMessage(userId));

        user.getPosts().forEach(post -> {
            post.setDeleted(true);
            postRepository.save(post);
            post.getComments().forEach(comment -> {
                comment.setDeleted(true);
                commentRepository.save(comment);
            });
        });

        user.setDeleted(true);
        user.setRegistrationStatus(RegistrationStatus.INACTIVE);
        userRepository.save(user);

        // Отправка сообщения в Kafka
        kafkaMessageService.sendUserDeletedMessage(user.getId(), user.getUsername());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetails(username, userRepository);
    }

    @Override
    public IamResponse<UserDTO> getUserInfo(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    UserDTO userDto = userMapper.toDto(user);
                    return IamResponse.createSuccessful(userDto);
                })
                .orElse(IamResponse.createFailed(ApiErrorMessage.NOT_FOUND_USER_ID.getMessage(username)));
    }

    @Override
    @Transactional(readOnly = true)
    public IamResponse<LinkedList<UserDTO>> findAllUsers() {
        List<User> users = userRepository.findAll(Sort.by("id"));
        LinkedList<UserDTO> userDto = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return IamResponse.createSuccessful(userDto);
    }
}
