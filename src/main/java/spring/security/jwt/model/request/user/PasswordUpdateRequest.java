package spring.security.jwt.model.request.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class PasswordUpdateRequest implements Serializable {
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmedPassword;

}
