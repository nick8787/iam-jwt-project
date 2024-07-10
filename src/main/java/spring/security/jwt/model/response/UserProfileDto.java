package spring.security.jwt.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import spring.security.jwt.model.enums.RegistrationStatus;
import spring.security.jwt.model.enums.RoleDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileDto implements Serializable {
    private Integer id;
    private String name;
    private String email;

    private RegistrationStatus registrationStatus;
    private LocalDateTime lastLogin;

    private String token;
    private String refreshToken;
    private List<RoleDto> roles;

}

