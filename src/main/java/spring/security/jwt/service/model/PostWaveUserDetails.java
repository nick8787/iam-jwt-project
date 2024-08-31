package spring.security.jwt.service.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import spring.security.jwt.model.enums.RegistrationStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Hidden
@Getter
public class PostWaveUserDetails implements UserDetails {
    private final String username; // emailAddress
    private final Integer userId;
    private final LocalDateTime lastUpdate;
    private final RegistrationStatus registrationStatus;
    private final String password; // token
    private final Set<GrantedAuthority> authorities;

    private final Integer tenantId;
    private final String tenantName;

    private final List<Integer> organizationIds;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public PostWaveUserDetails(
            String username, Integer userId, LocalDateTime lastUpdate, RegistrationStatus status, String token,
            Collection<? extends GrantedAuthority> authorities, Integer tenantId, String tenantName,
            List<Integer> organizationIds
    ) {
        this.username = username;
        this.userId = userId;
        this.lastUpdate = lastUpdate;
        this.registrationStatus = status;
        this.password = token;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.organizationIds = organizationIds;

        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to the set.
            // If the authority is null, it is a custom authority and should precede others.
            if (Objects.isNull(g2.getAuthority())) {
                return -1;
            }
            if (Objects.isNull(g1.getAuthority())) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

}
