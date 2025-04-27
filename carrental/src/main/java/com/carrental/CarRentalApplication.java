package com.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Car Rental service.
 * <p>
 * Boots up the Spring context and starts the embedded server.
 */
@SpringBootApplication
public class CarRentalApplication {

    /**
     * Application entry point.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }
}
