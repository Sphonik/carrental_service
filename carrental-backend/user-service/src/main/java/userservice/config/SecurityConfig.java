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

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository users;

    public SecurityConfig(UserRepository users) { this.users = users; }

    /* ---------- UserDetails ---------- */
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
/*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();   // <-- Klartext !
    }


    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService());
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    /* ---------- JSON-401 statt Browser-Popup ---------- */
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

    /* ---------- CORS (nur wenn du von extern zugreifst) ---------- */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:8080","http://127.0.0.1:8080, http://localhost:3000, http://127.0.0.1:3000"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    /* ---------- Filter-Chain ---------- */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users", "/api/v1/users/login").permitAll()
                        .requestMatchers("/", "/index.html", "/static/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
//                .httpBasic(basic -> basic.authenticationEntryPoint(restEntry()));
        return http.build();
    }
}
