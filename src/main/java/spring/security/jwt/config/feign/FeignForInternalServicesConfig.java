package spring.security.jwt.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.RequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import spring.security.jwt.service.model.AuthenticationConstants;


@Component
@RequiredArgsConstructor
public class FeignForInternalServicesConfig {
    private final ObjectMapper objectMapper;

    @Value("${internal.service.key}")
    private String internalServiceKey;
    @Value("${internal.iam.service.name}")
    private String internalIamServiceName;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate
                .header(AuthenticationConstants.ACCESS_KEY_HEADER_NAME, internalServiceKey)
                .header(AuthenticationConstants.ACCESS_SERVICE_HEADER_NAME, internalIamServiceName);
    }

    @Bean
    public JacksonEncoder encoder() {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public JacksonDecoder decoder() {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.INFO;
    }

}
