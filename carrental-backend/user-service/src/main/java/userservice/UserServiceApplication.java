package userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the User Service.
 * <p>
 * Annotated with {@link SpringBootApplication} to enable component scanning,
 * auto-configuration, and to bootstrap the Spring context.
 */
@SpringBootApplication
public class UserServiceApplication {

    /**
     * Application entry point.
     * <p>
     * Starts the Spring Boot application by initializing the application context
     * and launching the embedded server.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
