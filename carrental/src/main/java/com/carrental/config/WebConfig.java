package com.carrental.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for Cross-Origin Resource Sharing (CORS) settings.
 * <p>
 * Allows the frontend application to interact with the backend API under specified origins,
 * HTTP methods, and request headers.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Comma-separated list of allowed CORS origins, loaded from application properties.
     * <p>
     * Example property key: <code>cors.allowed-origins</code>
     */
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    /**
     * Configure CORS mappings for API endpoints.
     * <p>
     * - Applies to all <code>/api/**</code> paths.<br>
     * - Permits requests from specified origins.<br>
     * - Allows GET, POST, PUT, DELETE, and OPTIONS methods.<br>
     * - Accepts all headers and supports credentials.<br>
     * - Sets preflight cache duration to 3600 seconds.
     *
     * @param registry the {@link CorsRegistry} to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
