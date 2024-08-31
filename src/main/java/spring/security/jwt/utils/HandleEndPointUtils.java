package spring.security.jwt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import spring.security.jwt.model.constants.ApiErrorMessage;
import spring.security.jwt.model.exception.InvalidDataException;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HandleEndPointUtils {

    public static String handleGenericPath(String path) {
        if (Objects.isNull(path) || !path.startsWith("/")) {
            throw new InvalidDataException(ApiErrorMessage.INVALID_REQUESTED_PATH.getMessage(path));
        }

        StringBuilder result = new StringBuilder();
        String[] strings = path.split("/");
        for (String current : strings) {
            if (current.isEmpty()) {
                continue;
            }
            result.append("/").append(StringUtils.isNumeric(current) ? "{id}" : current);
        }
        return result.toString();
    }

}
