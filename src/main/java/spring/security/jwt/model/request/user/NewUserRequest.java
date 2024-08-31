package spring.security.jwt.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewUserRequest {
    @NotNull
    private String username;
    @NotEmpty
    private String password;
}
