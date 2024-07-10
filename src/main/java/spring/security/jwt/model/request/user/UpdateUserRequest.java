package spring.security.jwt.model.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UpdateUserRequest implements Serializable {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @Email
    private String emailAddress;

}
