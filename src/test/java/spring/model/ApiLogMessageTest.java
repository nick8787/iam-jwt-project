package spring.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import spring.security.jwt.model.constants.ApiLogMessage;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiLogMessageTest {

    @Test
    void getMessage_OK() {
        log.info(ApiLogMessage.USER_AND_ROLE_COMPATIBLE_AS.getMessage(null, null, null));
        log.info(ApiLogMessage.USER_AND_ROLE_COMPATIBLE_AS.getMessage(any(), any(), any()));
    }

}
