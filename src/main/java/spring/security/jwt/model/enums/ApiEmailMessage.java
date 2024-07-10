package spring.security.jwt.model.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiEmailMessage {
    RESET_PASSWORD("Hi %s\n\nPassword changing has been requested for your account.\n"
            + "If you want to change password please follow the link\n%s\n\n"
            + "If you didn't request - skip this message.\n\nThe link is available only 5min.\n"),
    CONFIRM_REGISTRATION("Hi %s\n\nRegistration confirming has been requested for your account.\n"
            + "If you want to confirm registration please follow the link\n%s\n\n"
            + "If you didn't request - skip this message.\n\nThe link is available only 5min.\n");

    private final String message;

    public String getMessage(String name, String link) {
        return String.format(message, name, link);
    }

}