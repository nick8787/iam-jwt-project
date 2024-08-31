package spring.security.jwt.security.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import spring.security.jwt.service.model.IamServiceUserRole;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component("accessSecurity")
@RequiredArgsConstructor
public class AccessSecurity {

    public boolean hasIamAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthenticated(authentication)) {
            log.warn("Unauthenticated access attempt");
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesOrEmpty(userDetails);

        if (hasRole(authorities, IamServiceUserRole.SUPER_ADMIN)) {
            log.info("Access granted for SUPER_ADMIN");
            return true;
        }

        if (hasRole(authorities, IamServiceUserRole.ADMIN)) {
            log.info("Access granted for ADMIN");
            return true;
        }

        log.warn("Access denied for user: {}", userDetails.getUsername());
        return false;
    }

    private boolean isAuthenticated(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            log.warn("Authentication principal is not an instance of UserDetails or authentication is null");
            return false;
        }
        return true;
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, IamServiceUserRole role) {
        return authorities.stream().anyMatch(auth -> Objects.equals(auth.getAuthority(), role.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesOrEmpty(UserDetails principal) {
        return Objects.isNull(principal.getAuthorities()) ? Collections.emptyList() : principal.getAuthorities();
    }
}
