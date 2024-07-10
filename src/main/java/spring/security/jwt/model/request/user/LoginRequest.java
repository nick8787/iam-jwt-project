package spring.security.jwt.model.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    @Email
    private String email;
    @NotEmpty
    private String password;

}
