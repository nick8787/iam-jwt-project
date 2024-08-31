package spring.security.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import spring.security.jwt.security.filter.JwtRequestFilter;
import spring.security.jwt.service.UserService;
import spring.security.jwt.service.model.IamServiceUserRole;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    private static final AntPathRequestMatcher[] NOT_SECURED_URLS = new AntPathRequestMatcher[]{
            new AntPathRequestMatcher("/iamServiceInner/**", GET),
            new AntPathRequestMatcher("/iamServiceInner/**", POST),
            new AntPathRequestMatcher("/auth/login", POST),
            new AntPathRequestMatcher("/auth/register", POST),
            new AntPathRequestMatcher("/auth/logout", GET),
            new AntPathRequestMatcher("/auth/refresh/token", GET),

            new AntPathRequestMatcher("/posts/all", GET),
            new AntPathRequestMatcher("/comments/all", GET),

            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/v3/api-docs.yaml"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/swagger-ui.html")
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Разрешаем все OPTIONS запросы
                .requestMatchers(NOT_SECURED_URLS).permitAll()
                .antMatchers("/secured/**", "/info").authenticated()
                .antMatchers("/oauth2/**").permitAll()

                .requestMatchers(get("/users")).hasAnyAuthority(adminAccessSecurityRoles())

                .anyRequest().authenticated()
                .and()
                .oauth2Login()  // Конфигурация OAuth2 входа
                .loginPage("/login")
                .defaultSuccessUrl("https://nick8787.github.io/post-wave-demo-project/", true)
                .failureUrl("/login?error=true")  // URL после неудачного входа
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private String[] adminAccessSecurityRoles() {
        return new String[]{
                IamServiceUserRole.SUPER_ADMIN.name(),
                IamServiceUserRole.ADMIN.name()
        };
    }

    private static AntPathRequestMatcher post(String pattern) {
        return new AntPathRequestMatcher(pattern, POST);
    }

    private static AntPathRequestMatcher put(String pattern) {
        return new AntPathRequestMatcher(pattern, PUT);
    }

    private static AntPathRequestMatcher get(String pattern) {
        return new AntPathRequestMatcher(pattern, GET);
    }

    private static AntPathRequestMatcher delete(String pattern) {
        return new AntPathRequestMatcher(pattern, DELETE);
    }
}
