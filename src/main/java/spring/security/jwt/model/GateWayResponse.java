package spring.security.jwt.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import spring.security.jwt.model.constants.ApiMessage;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Hidden
public class GateWayResponse<T extends Serializable> implements Serializable {
    private String message;
    private T payload;
    private boolean success;

    public static <T extends Serializable> GateWayResponse<T> createFailed(String message) {
        return new GateWayResponse<>(message, null, false);
    }

    public static GateWayResponse<String> createSuccessfulNewToken(String jwt) {
        return new GateWayResponse<>(ApiMessage.TOKEN_CREATED_OR_UPDATED.getMessage(), jwt, true);
    }

    @Override
    public String toString() {
        return "IamResponse{" +
                "Success=" + success +
                ", message='" + message + '\'' +
                '}';
    }

}
