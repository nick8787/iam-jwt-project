package spring.security.jwt.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.security.jwt.security.JwtTokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String LOGIN_PATH = "/auth/login";
    private static final String REGISTER_PATH = "/auth/register";

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        String requestURI = request.getRequestURI();

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String jwt = authHeader.substring(BEARER_PREFIX.length());
            try {
                if (!jwtTokenUtils.validateToken(jwt)) {
                    throw new ExpiredJwtException(null, null, "Token expired");
                }

                String username = jwtTokenUtils.getUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    List<SimpleGrantedAuthority> authorities = jwtTokenUtils.getRoles(jwt).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (ExpiredJwtException e) {
                if (isAuthEndpoint(requestURI)) {
                    String refreshedToken = jwtTokenUtils.refreshToken(jwt);
                    response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + refreshedToken);
                } else {
                    handleExpiredJwtException(response);
                    return;
                }
            } catch (SignatureException | MalformedJwtException e) {
                handleSignatureException(response);
                return;
            } catch (Exception e) {
                log.error("An unexpected error occurred during JWT processing", e);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.getWriter().write("An unexpected error occurred");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    @SneakyThrows
    private void handleExpiredJwtException(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Token expired");
    }

    @SneakyThrows
    private void handleSignatureException(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Invalid token signature");
    }

    private boolean isAuthEndpoint(String uri) {
        return uri.equals(LOGIN_PATH) || uri.equals(REGISTER_PATH);
    }
}
