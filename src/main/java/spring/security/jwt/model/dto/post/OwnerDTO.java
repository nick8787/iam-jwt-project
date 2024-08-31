package spring.security.jwt.model.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDTO implements Serializable {

    private Integer id;
    private String username;
    private String email;

}
