// src/main/java/com/carrental/config/SecurityConfig.java
package com.carrental.config;

import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;




/**
 * Security configuration for the Car Rental application.
 * <p>
 * Configures authentication, password encoding, and HTTP security rules.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepo;

    /**
     * Constructs the SecurityConfig with the given UserRepository.
     *
     * @param userRepo repository for accessing user data
     */
    public SecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Provides a {@link UserDetailsService} that loads user-specific data.
     * <p>
     * Retrieves a {@link User} by username and maps its role to a {@link SimpleGrantedAuthority}.
     *
     * @return a configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User u = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + u.getUserRole().name());
            return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    List.of(authority)
            );
        };
    }

    /**
     * Provides a {@link PasswordEncoder}.
     * <p>
     * Note: Currently uses {@link NoOpPasswordEncoder} for simplicity.
     * Replace with {@link BCryptPasswordEncoder} after migration.
     *
     * @return a PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configures the {@link DaoAuthenticationProvider} with the
     * {@link UserDetailsService} and {@link PasswordEncoder}.
     *
     * @return the configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Configures the HTTP security filter chain.
     * <p>
     * Disables CSRF, enables HTTP Basic authentication, and defines authorization rules:
     * <ul>
     *   <li>Allow POST requests to <code>/api/v1/users</code> for user registration.</li>
     *   <li>Require authentication for DELETE requests to <code>/bookings/**</code>.</li>
     *   <li>Permit all for static resources (<code>/static/**</code>), root (<code>/</code>), and <code>/index.html</code>.</li>
     *   <li>Require authentication for all other requests.</li>
     * </ul>
     *
     * @param http the HttpSecurity to configure
     * @return the built SecurityFilterChain
     * @throws Exception if an error occurs while building the filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").authenticated()
                        .requestMatchers("/static/**", "/", "/index.html").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
