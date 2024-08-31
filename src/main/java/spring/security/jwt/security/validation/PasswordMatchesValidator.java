package spring.security.jwt.security.validation;

import spring.security.jwt.model.request.user.RegistrationUserRequest;
import spring.security.jwt.utils.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationUserRequest> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // Initialization code, if necessary
    }

    @Override
    public boolean isValid(RegistrationUserRequest registrationUserDto, ConstraintValidatorContext context) {
        return registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword());
    }
}

