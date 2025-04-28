package carbookingservice;

import carbookingservice.repository.BookingRepository;
import carbookingservice.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Startup component that logs the initial counts of cars and bookings
 * to verify database connectivity and data seeding.
 */
@Component
public class StartupCheck implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupCheck.class);

    private final CarRepository carRepo;
    private final BookingRepository bookingRepo;

    /**
     * Constructs a new StartupCheck with the specified repositories.
     *
     * @param carRepo     repository for accessing Car entities
     * @param bookingRepo repository for accessing Booking entities
     */
    public StartupCheck(CarRepository carRepo, BookingRepository bookingRepo) {
        this.carRepo     = carRepo;
        this.bookingRepo = bookingRepo;
    }

    /**
     * Invoked on application startup to log the total number of cars and bookings.
     *
     * @param args runtime arguments (ignored)
     */
    @Override
    public void run(String... args) {
        log.info("🚗  Cars    : {}", carRepo.count());
        log.info("📑  Bookings: {}", bookingRepo.count());
    }
}
