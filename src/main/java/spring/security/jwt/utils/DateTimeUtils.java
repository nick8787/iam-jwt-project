package spring.security.jwt.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    public static LocalDateTime parse(@Nullable Long epochMillis) {
        if (Objects.isNull(epochMillis)) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
    }

    public static Long parse(@Nullable LocalDateTime timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return timestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}

