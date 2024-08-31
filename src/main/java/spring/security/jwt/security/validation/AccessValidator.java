package spring.security.jwt.security.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.model.exception.BadRequestException;
import spring.security.jwt.model.exception.DataExistException;
import spring.security.jwt.model.exception.InvalidPasswordException;
import spring.security.jwt.repositories.UserRepository;
import spring.security.jwt.service.model.IamServiceUserRole;
import spring.security.jwt.utils.PasswordUtils;

@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final UserRepository userRepository;

    public void validateAdminOrOwnerAccess(UserDetails currentUser, String targetUsername, String createdBy, String currentUsername, String errorMessage) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> IamServiceUserRole.isAdminRole(grantedAuthority.getAuthority()));

        if (createdBy == null) {
            if (!targetUsername.equals(currentUsername) && !isAdmin) {
                throw new BadRequestException(errorMessage);
            }
        } else {
            if (!targetUsername.equals(currentUsername) && !createdBy.equals(currentUsername) && !isAdmin) {
                throw new BadRequestException(errorMessage);
            }
        }
    }

    /**
     * Валидация доступа на создание комментария.
     * Позволяет любому авторизованному пользователю создавать комментарии, если пост существует.
     * @param currentUser Текущий пользователь
     * @param targetUsername Имя пользователя, которому принадлежит пост
     * @param errorMessage Сообщение об ошибке, если доступ запрещен
     */
    public void validateCommentCreationAccess(UserDetails currentUser, String targetUsername, String errorMessage) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> IamServiceUserRole.isAdminRole(grantedAuthority.getAuthority()));

        // Если пользователь не админ и не является владельцем поста, доступ все равно разрешен
        currentUser.getUsername();
    }

    public void validateViewAccess(UserDetails currentUser, String targetUsername, String createdBy) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> IamServiceUserRole.isAdminRole(grantedAuthority.getAuthority()));

        // Если пользователь не админ и не является создателем поста, доступ разрешен только на чтение
        if (!targetUsername.equals(currentUser.getUsername())) {
            currentUser.getUsername();
        }// Можно добавить дополнительную логику, если нужно, например логирование
    }

    public void validateUniqueFields(Integer userId, String username, String email, String phoneNumber) {
        if (userRepository.existsByUsernameAndIdNot(username, userId)) {
            throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(username));
        }
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new DataExistException(ApiErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage(email));
        }
        if (userRepository.existsByPhoneNumberAndIdNot(phoneNumber, userId)) {
            throw new DataExistException(ApiErrorMessage.PHONE_NUMBER_ALREADY_EXISTS.getMessage(phoneNumber));
        }
    }

    public void validateNewUserData(String username, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(ApiErrorMessage.MISMATCH_PASSWORDS.getMessage());
        }

        if (PasswordUtils.isNotValidPassword(password)) {
            throw new InvalidPasswordException(ApiErrorMessage.INVALID_PASSWORD.getMessage());
        }

        userRepository.findByUsername(username).ifPresent(user -> {
            throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(username));
        });

        if (userRepository.existsByEmail(email)) {
            throw new DataExistException(ApiErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage(email));
        }
    }

    public void validateNewUserRegistration(RegistrationUserRequest registrationUserRequest) {
        userRepository.findByUsername(registrationUserRequest.getUsername()).ifPresent(existingUser -> {
            throw new DataExistException(ApiErrorMessage.USERNAME_ALREADY_EXISTS.getMessage(registrationUserRequest.getUsername()));
        });
        userRepository.findByEmail(registrationUserRequest.getEmail()).ifPresent(existingUser -> {
            throw new DataExistException(ApiErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage(registrationUserRequest.getEmail()));
        });
    }

}
