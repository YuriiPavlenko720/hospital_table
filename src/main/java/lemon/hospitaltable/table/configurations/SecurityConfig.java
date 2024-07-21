package lemon.hospitaltable.table.configurations;

import lemon.hospitaltable.table.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
//@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, OidcUserService oidcUserService) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/", "/login**", "/swagger-ui**").permitAll()
//                        .requestMatchers("/privates/**").authenticated()
//                        .requestMatchers("/secrets/**").hasRole("ADMIN")
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .oidcUserService(oidcUserService)
//                        )
//                );
//        return http.build();
//    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }
}
