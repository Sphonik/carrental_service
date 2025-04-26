package carservice.repository;

import carservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    /** Alle Buchungen eines Users */
    List<Booking> findByBookedById(String userId);

    /**
     * Prüft, ob es eine überlappende Buchung gibt.
     * Mongo-API leitet die Methodennamen automatisch in die richtige Query um.
     */
    boolean existsByCarRentedIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String carId, LocalDate endDate, LocalDate startDate);
}
