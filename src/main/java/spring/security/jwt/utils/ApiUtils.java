package spring.security.jwt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import spring.security.jwt.model.constants.ApiConstants;
import spring.security.jwt.service.model.PostWaveUserDetails;

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

    public static String getEndPointMethod() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getMethod();
    }

    public static String getEndPointPath() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRequestURI();
    }

    public static Integer getUserIdFromAuthentication() {
        return ((PostWaveUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();
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
