package userservice.config;

import userservice.model.User;
import userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.List;

/**
 * Security configuration for the User service.
 * <p>
 * Defines user details service, authentication provider, CORS settings,
 * stateless session management, and JSON-based authentication entry point.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository users;

    public SecurityConfig(UserRepository users) {
        this.users = users;
    }

    /**
     * Provides a UserDetailsService that loads user data from the repository
     * and maps user roles to Spring Security authorities.
     *
     * @return configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User u = users.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));
            return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + u.getUserRole().name()))
            );
        };
    }

    /**
     * PasswordEncoder bean that performs no encoding (plaintext).
     * <p>
     * Suitable only for development or testing.
     *
     * @return a NoOpPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Authentication provider configured with the custom user details service
     * and password encoder.
     *
     * @return configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Returns a JSON-formatted 401 Unauthorized response
     * instead of triggering a browser authentication popup.
     *
     * @return AuthenticationEntryPoint producing JSON error details
     */
    private AuthenticationEntryPoint restEntry() {
        return (req, res, ex) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().printf("""
                {
                  "timestamp":"%s",
                  "status":401,
                  "error":"Unauthorized",
                  "message":"Authentication required",
                  "path":"%s"
                }""", Instant.now(), req.getRequestURI());
        };
    }

    /**
     * Configures CORS settings to allow specified origins, HTTP methods,
     * headers, and credentials sharing.
     *
     * @return CorsConfigurationSource for handling CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:8090",
                "http://127.0.0.1:8090",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    /**
     * Configures the security filter chain to:
     * <ul>
     *   <li>Disable CSRF protection</li>
     *   <li>Enable CORS</li>
     *   <li>Enforce stateless session management</li>
     *   <li>Allow customization of authorization rules and entry point</li>
     * </ul>
     *
     * @param http the HttpSecurity builder
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Additional authorization rules and HTTP Basic entry point
        // can be enabled here if needed:
        // .authorizeHttpRequests(...)
        // .httpBasic(basic -> basic.authenticationEntryPoint(restEntry()));

        return http.build();
    }
}
