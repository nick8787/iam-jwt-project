package spring.security.jwt.model.request.user;

import lombok.Data;
import spring.security.jwt.utils.PasswordMatches;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@PasswordMatches
public class RegistrationUserRequest implements Serializable {
    @NotBlank
    private String username;

    @NotNull
    @Email
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;
}
