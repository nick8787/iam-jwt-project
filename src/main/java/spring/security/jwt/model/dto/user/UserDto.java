package spring.security.jwt.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.model.enums.RoleDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private LocalDateTime lastLogin;

    private RegistrationStatus registrationStatus;
    private List<RoleDto> roles;

}
