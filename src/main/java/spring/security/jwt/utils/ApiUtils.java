package spring.security.jwt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import spring.security.jwt.model.constants.ApiConstants;

import javax.servlet.http.Cookie;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiUtils {

    public static String generateUuidWithoutDash() {
        return UUID.randomUUID().toString().replace(ApiConstants.DASH, StringUtils.EMPTY);
    }

    public static String getMethodName() {
        try {
            return new Throwable().getStackTrace()[1].getMethodName();
        } catch (Exception cause) {
            return ApiConstants.UNDEFINED;
        }
    }

    public static Cookie createAuthCookie(String value) {
        Cookie authorizationCookie = new Cookie(HttpHeaders.AUTHORIZATION, value);
        authorizationCookie.setHttpOnly(true);
        authorizationCookie.setSecure(true);
        authorizationCookie.setPath("/");
        authorizationCookie.setMaxAge(300);
        return authorizationCookie;
    }

    public static Cookie blockAuthCookie() {
        Cookie authorizationCookie = new Cookie(HttpHeaders.AUTHORIZATION, null);
        authorizationCookie.setHttpOnly(true);
        authorizationCookie.setSecure(true);
        authorizationCookie.setPath("/");
        authorizationCookie.setMaxAge(0);
        return authorizationCookie;
    }


}
