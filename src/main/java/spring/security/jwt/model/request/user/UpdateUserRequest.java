package spring.security.jwt.model.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UpdateUserRequest implements Serializable {
    @NotBlank
    private String username;

    @NotNull
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?[\\d\\s\\-()]{10,20}$", message = "Invalid phone number")
    private String phoneNumber;

}
