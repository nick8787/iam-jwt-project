package spring.security.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
//    private static final long MAX_AGE_SECS = 3600;
//
//    private final String[] allowedOrigins = new String[] {
//            "http://localhost:63342",
//            "http://185.69.155.205:8100",
//            "https://nick8787.github.io",
//            "https://my-website.testing87.online"
//    };
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns(allowedOrigins)
//                .allowedMethods(CorsConfiguration.ALL)
//                .allowedHeaders(CorsConfiguration.ALL)
//                .allowCredentials(true)
//                .maxAge(MAX_AGE_SECS);
//    }

}
