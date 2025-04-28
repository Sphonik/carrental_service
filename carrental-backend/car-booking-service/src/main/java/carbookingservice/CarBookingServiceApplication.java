package carbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Car Booking Service.
 * <p>
 * Annotated with @SpringBootApplication to enable component scanning,
 * auto-configuration, and to bootstrap the Spring context.
 */
@SpringBootApplication
public class CarBookingServiceApplication {

    /**
     * Application entry point.
     * <p>
     * Starts the Spring Boot application by initializing the application context
     * and launching the embedded server.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(CarBookingServiceApplication.class, args);
    }
}
