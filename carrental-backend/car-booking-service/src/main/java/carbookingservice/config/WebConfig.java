package carbookingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures CORS settings for the web application, applying them to API endpoints.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * List of allowed origins for CORS, injected from the
     * <code>cors.allowed-origins</code> application property.
     */
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    /**
     * Adds CORS mappings to allow cross-origin requests on all API paths.
     * <p>
     * Permits the configured origins, common HTTP methods, all headers,
     * credentials sharing, and sets the preflight cache duration.
     *
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
