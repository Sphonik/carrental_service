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

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepo;

    public SecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User u = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            // map your roles to GrantedAuthority
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority("ROLE_" + u.getUserRole().name());
            return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    List.of(auth)
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // for a real app you’d switch this to BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
        //return new BCryptPasswordEncoder(/* strength: 10 */); TODO add it after migration
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService());
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // 1) Erlaube das Anlegen neuer User
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        // 2) Deine bisherigen Freigaben
                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").authenticated() // Nur für Admins

                        .requestMatchers("/static/**", "/", "/index.html").permitAll()
                        // 3) alles andere bleibt geschützt
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
