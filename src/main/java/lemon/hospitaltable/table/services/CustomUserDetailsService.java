package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.CustomUserDetails;
import lemon.hospitaltable.table.objects.User;
import lemon.hospitaltable.table.repositories.RolesRepositoryInterface;
import lemon.hospitaltable.table.repositories.UsersRepositoryInterface;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UsersRepositoryInterface usersRepository;
    private final RolesRepositoryInterface rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Loading user by username: {}", username);

        User user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        logger.info("User found: {}", user);

        return new CustomUserDetails(user, rolesRepository);
    }
}
