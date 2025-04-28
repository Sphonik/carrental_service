package carbookingservice.repository;

import carbookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for managing Booking entities in MongoDB.
 * <p>
 * Provides methods to retrieve bookings by user and to check for overlapping bookings.
 */
public interface BookingRepository extends MongoRepository<Booking, String> {

    /**
     * Retrieves all bookings made by the specified user.
     *
     * @param userId identifier of the user
     * @return list of Booking entities for the user
     */
    List<Booking> findByBookedById(String userId);

    /**
     * Checks whether there is an existing booking for the given car
     * that overlaps with the specified date range.
     *
     * @param carId     identifier of the car to check
     * @param endDate   end date of the desired booking period (inclusive)
     * @param startDate start date of the desired booking period (inclusive)
     * @return true if an overlapping booking exists, false otherwise
     */
    boolean existsByCarRentedIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String carId, LocalDate endDate, LocalDate startDate);
}
