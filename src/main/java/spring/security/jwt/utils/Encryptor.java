package spring.security.jwt.utils;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public final class Encryptor {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private Encryptor() {
        throw new IllegalStateException("Utility class");
    }

    public static String sha(String original) {
        return Hashing.sha256().hashString(original, UTF_8).toString();
    }

}
