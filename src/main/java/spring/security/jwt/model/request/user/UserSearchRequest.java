package spring.security.jwt.model.request.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchRequest implements Serializable {
    private Integer tenantId;

}
