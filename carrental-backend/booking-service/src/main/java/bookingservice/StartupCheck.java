package bookingservice;

import bookingservice.repository.BookingRepository;
import bookingservice.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupCheck implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupCheck.class);

    private final CarRepository     carRepo;
    private final BookingRepository bookingRepo;

    public StartupCheck(CarRepository carRepo, BookingRepository bookingRepo) {
        this.carRepo     = carRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public void run(String... args) {
        log.info("🚗  Cars    : {}", carRepo.count());
        log.info("📑  Bookings: {}", bookingRepo.count());
    }
}
