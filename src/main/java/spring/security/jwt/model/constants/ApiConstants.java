package spring.security.jwt.model.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {

    public static final String DEFAULT_WHITESPACES_BEFORE_STACK_TRACE = "        ";
    public static final String FOUR_SPACES = "    ";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String SLASH = "/";
    public static final String DASH = "-";
    public static final String BREAK_LINE = "\n";
    public static final String EMPTY = "EMPTY";
    public static final String SUCCESSFUL = "Successful";
    public static final String FAILED = "Failed";
    public static final String UNDEFINED = "undefined";
    public static final String DEFAULT_PACKAGE_NAME = "com.post_wave.iam";
    public static final String SECURITY_PACKAGE_NAME = "org.springframework.security";
    public static final String TIME_ZONE_PACKAGE_NAME = "java.time.zone";
    public static final String DASH_DELETED = "-deleted";
    public static final String JAVAX_VALIDATION_PACKAGE_NAME = "javax.validation";
    public static final String SPRING_WEB_BIND_PACKAGE_NAME = "org.springframework.web.bind";
    public static final String APACHE_COMMONS_LANG3_PACKAGE_NAME = "org.apache.commons.lang3";
    public static final String PASSWORD_ALL_CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
    public static final String PASSWORD_LETTERS_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String PASSWORD_LETTERS_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String PASSWORD_DIGITS = "0123456789";
    public static final String PASSWORD_CHARACTERS = "~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
    public static final int REQUIRED_MIN_PASSWORD_LENGTH = 8;
    public static final int REQUIRED_MIN_LETTERS_NUMBER_EVERY_CASE_IN_PASSWORD = 1;
    public static final int REQUIRED_MIN_DIGITS_NUMBER_IN_PASSWORD = 1;
    public static final int REQUIRED_MIN_CHARACTERS_NUMBER_IN_PASSWORD = 1;

}
