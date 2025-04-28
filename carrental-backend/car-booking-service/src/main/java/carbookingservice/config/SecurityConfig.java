package carbookingservice.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.List;

/**
 * Configures application security, including CORS settings,
 * stateless session management, and a JSON-based authentication entry point.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Entry point that returns a JSON-formatted 401 Unauthorized response
     * instead of triggering a basic-auth browser popup.
     *
     * @return an AuthenticationEntryPoint producing JSON error details
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
     * Defines CORS configuration allowing cross-origin requests from
     * specified local development origins and permitting common HTTP methods.
     *
     * @return the CorsConfigurationSource for the application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:8080",
                "http://127.0.0.1:8080",
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
     *   <li>Disable CSRF protection.</li>
     *   <li>Enable CORS with the defined configuration.</li>
     *   <li>Enforce stateless session management.</li>
     *   <li>(Authentication rules can be uncommented and customized as needed.)</li>
     * </ul>
     *
     * @param http the HttpSecurity builder to configure
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Further request authorization rules and authentication entry point
        // can be enabled here when integrating user authentication.

        return http.build();
    }
}
