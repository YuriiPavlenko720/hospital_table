package lemon.hospitaltable.table.configurations;

import jakarta.servlet.http.HttpServletResponse;
import lemon.hospitaltable.table.controllers.RunCheckController;
import lemon.hospitaltable.table.objects.security.Role;
import lemon.hospitaltable.table.objects.security.User;
import lemon.hospitaltable.table.repositories.RolesRepositoryInterface;
import lemon.hospitaltable.table.repositories.UsersRepositoryInterface;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final UsersRepositoryInterface usersRepository;
    private final RolesRepositoryInterface rolesRepository;
    private static final Logger logger = LoggerFactory.getLogger(RunCheckController.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(this.oidcUserService())
                        )
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/**", "/h2-console/**", "/flyway/**")  // Disable CSRF check
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler())
                );
        return http.build();
    }

    private OidcUserService oidcUserService() {
        final OidcUserService delegate = new OidcUserService();
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
                OidcUser oidcUser = delegate.loadUser(userRequest);

                String email = oidcUser.getEmail();
                Optional<User> user = usersRepository.findByEmail(email);
                Set<SimpleGrantedAuthority> mappedAuthorities;

                if (user.isPresent()) {
                    Role role = rolesRepository.findById(user.get().roleId()).orElseThrow();
                    logger.info("User get the role ROLE_{}", role.name());
                    mappedAuthorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
                } else {
                    // If the user is not found in the database, give him minimum authority (GUEST)
                    logger.info("User get the role ROLE_GUEST");
                    mappedAuthorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_GUEST"));
                }

                return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            logger.info("User`s access denied");
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(
                    "<h1>Access Denied</h1><p>You do not have permission to access this page.</p>"
            );
        };
    }
}
