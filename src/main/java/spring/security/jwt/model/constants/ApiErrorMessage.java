package spring.security.jwt.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    INVALID_USER_OR_PASSWORD("Invalid username or password."),
    INVALID_USER_REGISTRATION_STATUS("Invalid user registration status: %s. "),
    INTERNAL_ERROR("Internal error"),

    NOT_FOUND_TENANT_LOGO_ID("TenantLogoId %s not found."),
    NOT_FOUND_TENANT_ID("TenantId %s not found."),
    NOT_FOUND_USER_ID("UserId %s not found."),
    NOT_FOUND_USER_ID_AND_CREATED_BY("User not found with Id: %s created by: %s"),
    NOT_FOUND_POST_ID("PostId %s not found."),
    NOT_FOUND_USER_NAME("Username %s not found."),
    NOT_FOUND_EMAIL("Email address %s not found."),
    NOT_FOUND_HASH("Requested hash not found."),
    NOT_FOUND_REFRESH_TOKEN("Refresh token not found."),
    NOT_FOUND_USER_ROLE("User Role not found."),
    NOT_FOUND_ROLE("RoleId %s not found."),
    NOT_FOUND_PERMISSION("PermissionId %s not found."),
    NOT_FOUND_ORGANIZATION("OrganizationId %s not found."),
    NOT_FOUND_SERVICE("Service '%s' not found."),
    NOT_FOUND_SERVICE_BY_METHOD_PATH("Service by method %s and path '%s' not found."),
    NOT_FOUND_USER_ID_WITH_TENANT_AND_ORGANIZATION("UserId %s not found."),
    NOT_FOUND_COMMENT_ID("CommentId %s not found."),

    HAVE_NO_ACCESS_TO_VIEW_USER("Have no access to view user with id: %s"),
    HASH_WAS_ALREADY_USED("Hash(id: %s) was already used."),
    HAVE_NO_ACCESS_TO_UPDATE("You don't have access to update it"),
    HAVE_NO_ACCESS_TO_DELETE("You don't have access to delete user with Id: %s"),
    HAVE_NO_ACCESS_TO_UPDATE_COMMENT_AND_POST("You don't have access to update comment with id: %s and post with id: %s"),
    HAVE_NO_ACCESS_TO_UPDATE_POST("You don't have access to update post with id: %s"),
    HAVE_NO_ACCESS_TO_VIEW_POST("You don't have access to view post with id: %s"),
    HAVE_NO_ACCESS_TO_VIEW_COMMENT("You don't have access to view comment with id: %s"),
    HAVE_NO_ACCESS_TO_CREATE_COMMENT("You don't have access to create comment with postId: %s"),
    HAVE_NO_ACCESS_TO_UPDATE_COMMENT("You don't have access to update comment with id: %s"),
    HAVE_NO_ACCESS_TO_DELETE_COMMENT("You don't have access to delete comment with id: %s"),
    USERNAME_ALREADY_EXISTS("User name: %s already exists"),
    POST_ALREADY_EXISTS("Post with the title: '%s' already exists."),
    MISMATCH_PASSWORDS("Password does not match"),
    USER_EMAIL_ALREADY_EXISTS("Email address: %s already exists"),
    ORGANIZATION_ALREADY_EXISTS("TenantId %s already has Organization with name '%s'"),
    ROLE_ALREADY_ASSIGNED("RoleId %s is already assigned to the userId %s. "),
    UNDEFINED_USER_ROLE("Role of the user(id %s) is undefined"),
    PHONE_NUMBER_ALREADY_EXISTS("Phone number %s already exists"),

    REQUEST_FOR_ALL_PERMISSIONS_WITH_LIST("If all permissions are granted, permissionIds must be empty. Size = %s "),
    REQUEST_SPECIFIC_PERMISSIONS_WITHOUT_LIST("If all permissions are not granted, permissionIds must be specified "),
    REQUEST_FOR_ALL_TENANTS_WITH_ID("If access to all tenants are granted, tenantId must be null. TenantId = %s "),
    REQUEST_FOR_ALL_TENANTS_FROM_TENANT_USER("Tenant user(id %s) is not allowed to request access across the tenants. "),
    REQUEST_FOR_SPECIFIC_TENANT_WITHOUT_ID("If access to all tenants are not granted, tenantId must be specified "),
    INVALID_USERS("Users not found or they have not Active registration status: %s. "),
    INVALID_ADDRESS("All address fields must be specified. %s"),
    INVALID_TOKEN_PROVIDED("Invalid token provided"),
    INVALID_REQUESTED_PATH("Path '%s' must start with '/'."),
    INVALID_EMAIL("Invalid email. "),
    INVALID_PASSWORD("Invalid password. It must have: "
            + "length at least " + ApiConstants.REQUIRED_MIN_PASSWORD_LENGTH + ", including "
            + ApiConstants.REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD + " letter(s) in upper and lower cases, "
            + ApiConstants.REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD + " character(s), "
            + ApiConstants.REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD + " digit(s). "),
    EMPTY_LIST_DENIED("Empty list is denied. "),

    PASSWORDS_DO_NOT_MATCH("Passwords do not match. "),
    HASH_IS_EXPIRED("Hash is expired. "),
    COULD_NOT_SEND_EMAIL("Error during email sending. "),
    UPLOAD_FAILED("Failed to upload file. "),
    USERS_NOT_ALLOWED_ADD_PERMISSION_HE_DOESNT_HAVE("Users are not allowed to add permissions they don't have "),
    TENANT_USER_ACCESS_TO_ANOTHER_TENANT("Tenant user is allowed to give access only to his tenant. "),
    ROLE_ALREADY_EXISTS("%s already exists. "),
    COULD_NOT_ADD_USERS_TO_NEW_ORGANIZATION("Could not add all specified users '%s' to new organization. "),

    AUTHENTICATION_FAILED_FOR_USER("Authentication failed for user: %s. "),
    USER_ACCESS_DENIED("User %s doesn't have access to requested resource '%s %s'. "),
    ACCESS_WITHOUT_API_SECRET("Unauthorized Access, you should pass only with required API secret"),
    ACCESS_THROUGH_NOT_API_SERVICE("Unauthorized Access, you should pass through the API service");

    private final String message;

    public String getMessage(List<Integer> arg1) {
        return String.format(message, arg1.isEmpty() ? "EMPTY" : Arrays.toString(arg1.toArray()));
    }

    public String getMessage(Integer arg1) {
        return String.format(message, Objects.nonNull(arg1) ? arg1 : "NULL");
    }

    public String getMessage(String arg1) {
        return String.format(message, Objects.nonNull(arg1) ? arg1 : "NULL");
    }

    public String getMessage(Object arg1, Object arg2) {
        return String.format(
                message,
                Objects.nonNull(arg1) ? arg1 : "NULL",
                Objects.nonNull(arg2) ? arg2 : "NULL"
        );
    }

    public String getMessage(Object arg1, Object arg2, Object arg3) {
        return String.format(
                message,
                Objects.nonNull(arg1) ? arg1 : "NULL",
                Objects.nonNull(arg2) ? arg2 : "NULL",
                Objects.nonNull(arg3) ? arg3 : "NULL"
        );
    }

}
