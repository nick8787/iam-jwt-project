package spring.security.jwt.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiMessage {
    EMAIL_WAS_SENT("Email was sent"),
    REGISTRATION_IS_CONFIRMED("Registration is confirmed"),
    TOKEN_CREATED_OR_UPDATED("User's token has been created or updated"),
    PASSWORD_HAS_BEEN_UPDATED("Password has been updated"),
    NOT_FOUND_PARTNERS_PARTY_ID_AND_COUNTRY_CODE("Partner with partyId '%s' and countryCode '%s'"),
    TOKEN_A_NOT_FOUND("TokenA '%s' not found."),
    TOKENS_NOT_MATCH("Tokens not match."),
    DISABLED_PARTNER("Partner is disabled."),
    INVALID_TENANT_STATUS("Tenant is not active or deleted."),
    ;

    private final String message;

    public String getMessage() {
        return message;
    }

    public String getMessage(Object arg1) {
        return String.format(message, arg1);
    }

    public String getMessage(Object arg1, Object arg2) {
        return String.format(message, arg1, arg2);
    }

}
