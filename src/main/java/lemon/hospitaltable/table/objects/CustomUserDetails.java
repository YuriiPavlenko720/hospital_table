package lemon.hospitaltable.table.objects;

import lemon.hospitaltable.table.repositories.RolesRepositoryInterface;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);

    private final User user;
    private final RolesRepositoryInterface rolesRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Optional<Role> role = rolesRepository.findById(user.roleId());
        if (role.isPresent()) {
            String roleName = "ROLE_" + role.get().name();

            logger.info("Assigning role: {}", roleName);

            return Collections.singletonList(new SimpleGrantedAuthority(roleName));
        }

        logger.warn("No role found for roleId: {}", user.roleId());

        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.email();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
