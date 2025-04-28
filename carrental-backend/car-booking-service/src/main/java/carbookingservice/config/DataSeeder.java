package carbookingservice.config;

import carbookingservice.model.Car;
import carbookingservice.model.FuelType;
import carbookingservice.repository.BookingRepository;
import carbookingservice.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Component that initializes demo data for cars (and bookings in future)
 * when the application starts, if the repositories are empty.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    private final CarRepository carRepo;
    private final BookingRepository bookingRepo;

    /**
     * Creates a new DataSeeder with the required repositories.
     *
     * @param carRepo     repository for storing and retrieving Car entities
     * @param bookingRepo repository for storing and retrieving Booking entities
     */
    public DataSeeder(CarRepository carRepo,
                      BookingRepository bookingRepo) {
        this.carRepo     = carRepo;
        this.bookingRepo = bookingRepo;
    }

    /**
     * Entry point that runs after the Spring Boot application context is loaded.
     * <p>
     * Seeds car data only if no cars are present.
     *
     * @param args runtime arguments (ignored)
     */
    @Override
    public void run(String... args) {
        if (carRepo.count() == 0) {
            seedCars();
        }
    }

    /**
     * Inserts a predefined set of demo cars into the database.
     * <p>
     * Each car includes make, model, year, color, fuel type, availability,
     * base price per day in USD, and location.
     */
    private void seedCars() {
        List<Car> cars = List.of(
                new Car("Tesla",   "Model 3", 2022, "white", FuelType.ELECTRIC, true,
                        new BigDecimal("69.90"), "Vienna Airport"),
                new Car("BMW",     "320d",    2021, "black", FuelType.DIESEL,   true,
                        new BigDecimal("59.90"), "Vienna City"),
                new Car("VW",      "Golf",    2019, "blue",  FuelType.PETROL,   false,
                        new BigDecimal("39.90"), "Salzburg Hbf"),
                new Car("Škoda",   "Octavia", 2020, "gray",  FuelType.PETROL,   true,
                        new BigDecimal("44.90"), "Graz Airport"),
                new Car("Renault", "Zoe",     2023, "green", FuelType.ELECTRIC, true,
                        new BigDecimal("49.90"), "Linz City")
        );
        carRepo.saveAll(cars);
        LOG.info("🚗  {} demo cars inserted", cars.size());
    }
}
