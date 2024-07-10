package spring.security.jwt.model;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import spring.security.jwt.model.constants.ApiMessage;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IamResponse<P extends Serializable> implements Serializable {
    private String message;
    private P payload;
    private boolean success;

    public static <P extends Serializable> IamResponse<P> createFailed(String message) {
        return new IamResponse<>(message, null, false);
    }

    public static <P extends Serializable> IamResponse<P> createSuccessful(P payload) {
        return new IamResponse<>(StringUtils.EMPTY, payload, true);
    }

    public static <P extends Serializable> IamResponse<P> createSuccessfulWithNewToken(P payload) {
        return new IamResponse<>(ApiMessage.TOKEN_CREATED_OR_UPDATED.getMessage(), payload, true);
    }

}

