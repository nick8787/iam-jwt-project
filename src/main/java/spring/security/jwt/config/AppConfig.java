package spring.security.jwt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import spring.security.jwt.model.GateWayRequest;
import spring.security.jwt.model.GateWayResponse;
import spring.security.jwt.service.model.IcasaUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "'MYKOLA.SHCH' REST API", version = "1.0",
                description = "'MYKOLA.SHCH' REST API endpoints",
                contact = @Contact(name = "MYKOLA.SHCH")),
        security = { @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = HttpHeaders.AUTHORIZATION)}
)
@SecurityScheme(
        name = HttpHeaders.AUTHORIZATION,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class AppConfig {
    @Value("http://localhost:8189")
    private String firstServer;
    @Value("${swagger.servers.second:#{null}}")
    private String secondServer;

    @Bean
    public GroupedOpenApi publicApi() {
        SpringDocUtils.getConfig().replaceWithClass(LocalDateTime.class, Long.class);
        SpringDocUtils.getConfig().replaceWithClass(LocalDate.class, Long.class);
        SpringDocUtils.getConfig().replaceWithClass(Date.class, Long.class);

        SpringDocUtils.getConfig().addResponseWrapperToIgnore(GateWayResponse.class);
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(GateWayRequest.class);

        SpringDocUtils.getConfig().addResponseTypeToIgnore(GrantedAuthority.class);
        SpringDocUtils.getConfig().addResponseTypeToIgnore(IcasaUserDetails.class);

        return GroupedOpenApi.builder()
                .group("iam-service")
                .packagesToScan("spring.security.jwt.controller")
                .build();
    }

    @Bean
    public OpenApiCustomiser createOpenApiCustomiser() {
        return openApi -> {
            List<Server> servers = new LinkedList<>();
            String description = " + end-point from controller(without service name). Example: ";
            if (Objects.nonNull(firstServer)) {
                String example = firstServer + "/users";
                servers.add(new Server().url(firstServer).description(description + example));
            }
            if (Objects.nonNull(secondServer)) {
                String example = secondServer + "/users";
                servers.add(new Server().url(secondServer).description(description + example));
            }
            openApi.servers(servers);
        };
    }

}
