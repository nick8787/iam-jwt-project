package spring.security.jwt.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiLogMessage {
    UPLOAD_FAILED("Failed to upload file. "),
    SERVER_PORT_FROM_REQUEST("Request from 'Server:Port': '{}:{}'"),
    INTERNAL_HEADER_FROM_REQUEST("Header '{}': '{}'"),
    RESET_PASSWORD_LINK_HAS_BEEN_SENT("Reset password email has been sent. {}"),
    CONFIRM_REGISTRATION_LINK_HAS_BEEN_SENT("Confirm registration email has been sent. {}"),
    NAME_OF_CURRENT_METHOD("{} method executing. "),
    HAS_ACCESS_TO_END_POINT("Access to IAM '{} {}' by user {} is granted. "),
    ACCESS_STATUS_TO_END_POINT("Access to IAM '{} {}' by user {} is granted: {}. "),
    EMAIL_HAS_NOT_BEEN_SENT("Email has not been sent. "),
    ACCESS_THROUGH_NOT_API_SERVICE("Unauthorized Access, access must be through the API service('{}')"),
    COULD_NOT_SEND_MESSAGE_TO_UTILS_SERVICE("Couldn't send message to the utils-service: {}"),
    COULD_NOT_SEND_TO_CHARGE_POINT_SERVICE("Couldn't send message to the chargePoint-service: {}"),
    COULD_NOT_SEND_TO_IAM_OUT_TOPIC("Couldn't send message to the iam-out topic: {}"),
    USER_ACCESS_DENIED("User {} doesn't have access to requested resource '{} {}'. "),
    USER_HAS_NO_ACCESS_TO_USER("User {} doesn't have access to requested user {}. "),
    JWT_TOKEN_AUTO_UPDATED_FOR_AUTH_COOKIES("Jwt is auto-updated, security cookies flow. \nOld: {}\n New: {}"),
    TENANT_IDS_NOT_MATCH("Principal tenantId {} doesn't match with requested tenantId {} and user's tenantId {}."),
    USER_AND_ROLE_COMPATIBLE_AS("UserId {} is compatible with RoleId {}. {}"),
    TOKENS_NOT_MATCH("Tokens '{}', '{}' not match"),
    DISABLED_PARTNER("Partner with id '{}' is disabled."),
    INVALID_TENANT_STATUS("Tenant '{}' has invalid status. Deleted '{}', active '{}'"),
    NOT_FOUND_PARTNER_FOR_ROAMING_TOKEN("Partner with partyId '{}' and countryCode '{}' not found"),
    NOT_FOUND_CONTRACT_BETWEEN_PARTNER_TENANT("Contract between PartnerId '{}' and tenantId '{}' not found"),
    ROAMING_TOKEN_CREATED("Roaming token created: id '{}', uid '{}', message: '{}'."),
    ROAMING_TOKEN_UPDATED("Roaming token updated: id '{}', uid '{}', message: '{}'."),
    ROAMING_TOKEN_DOES_NOT_BELONG_TO_PARTNER("Roaming tokenId '{}' doesn't belong to partnerId '{}'"),
    ;

    private final String value;

    public String getMessage(Object arg1, Object arg2, Object arg3) {
        return String.format(value.replace("{}", "%s"), arg1, arg2, arg3);
    }

}
