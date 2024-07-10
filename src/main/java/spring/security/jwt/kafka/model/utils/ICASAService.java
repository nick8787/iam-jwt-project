package spring.security.jwt.kafka.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ICASAService {
    GATEWAY_SERVICE("gateway-service"),
    IAM_SERVICE("iam-service"),
    CHARGE_POINT_SERVICE("charge-point-service"),
    SESSION_SERVICE("session-service"),
    TARIFF_SERVICE("tariff-service"),
    CENTRAL_SERVICE("central-service"),
    OCPI_INTEGRATION_SERVICE("ocpi-integration-service"),
    HUBJECT_SERVICE("hubject-service"),
    UNDEFINED_SERVICE("Undefined-service");

    private final String value;
}
