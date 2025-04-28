package userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures global CORS settings for the User service's web endpoints.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * List of allowed origins for cross-origin requests, injected from
     * <code>cors.allowed-origins</code> application property.
     */
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    /**
     * Adds CORS mappings to allow cross-origin HTTP requests to API endpoints.
     * <p>
     * Applies to paths matching <code>/api/**</code>, permitting the specified
     * origins, HTTP methods, all headers, credentials sharing, and sets the
     * maximum preflight cache duration.
     *
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "https://carrental-frontend-app.azurewebsites.net"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
