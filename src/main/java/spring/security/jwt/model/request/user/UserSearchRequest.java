package spring.security.jwt.model.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UserSearchRequest implements Serializable {
    private String username;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

}
