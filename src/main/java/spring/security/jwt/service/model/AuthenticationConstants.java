package spring.security.jwt.service.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationConstants {

    public static final String TOKEN_PREFIX = "Bearer";
    public static final String ACCESS_SERVICE_HEADER_NAME = "service-name";
    public static final String ACCESS_KEY_HEADER_NAME = "key";
    public static final String USER_EMAIL = "email";
    public static final String USER_REGISTRATION_STATUS = "userRegistrationStatus";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String ROLE = "role";
    public static final String USER_ID = "userId";
    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_NAME = "tenantName";

}
