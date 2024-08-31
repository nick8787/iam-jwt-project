package spring.security.jwt.kafka.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostWaveService {
    GATEWAY_SERVICE("gateway-service"),
    IAM_SERVICE("iam-service"),
    UNDEFINED_SERVICE("Undefined-service");

    private final String value;
}
