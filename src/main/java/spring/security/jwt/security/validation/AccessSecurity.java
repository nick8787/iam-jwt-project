package spring.security.jwt.security.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.service.model.IamServiceUserRole;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component("accessSecurity")
@RequiredArgsConstructor
public class AccessSecurity {

    public boolean hasIamAccess(UserDetails principal) {
        if (principal == null) {
            log.error("Principal is null");
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            log.error("No authorities found for user: {}", principal.getUsername());
            return false;
        }

        boolean isAdmin = hasAdminRole(authorities);
        log.debug("User: {} is admin: {}", principal.getUsername(), isAdmin);

        if (isAdmin) {
            log.info("{} (Admin Access)", ApiLogMessage.HAS_ACCESS_TO_END_POINT.getValue());
            return true;
        } else {
            log.warn("{} - Access denied", principal.getUsername());
            log.info(ApiLogMessage.USER_ACCESS_DENIED.getValue());
            return false;
        }
    }

    private boolean hasAdminRole(Collection<? extends GrantedAuthority> authorities) {
        boolean hasRole = authorities.stream().anyMatch(
                a -> Objects.equals(a.getAuthority(), IamServiceUserRole.ROLE_ADMIN.getRole())
        );
        log.debug("Admin role present: {}", hasRole);
        return hasRole;
    }

    private boolean hasUserRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().anyMatch(
                a -> Objects.equals(a.getAuthority(), IamServiceUserRole.ROLE_USER.getRole())
        );
    }

}
