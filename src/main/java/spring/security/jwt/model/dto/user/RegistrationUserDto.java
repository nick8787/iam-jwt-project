package spring.security.jwt.model.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RegistrationUserDto implements Serializable {

    @NotBlank
    private String username;
    @NotNull
    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;

}
