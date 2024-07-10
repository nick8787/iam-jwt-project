package spring.security.jwt.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    @NotNull
    private String username;
    @NotEmpty
    private String password;
}
