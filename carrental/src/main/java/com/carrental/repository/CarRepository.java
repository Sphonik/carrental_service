package com.carrental.repository;

import com.carrental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for {@link Car} entities.
 * <p>
 * Provides methods to retrieve cars based on availability status.
 */
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Retrieves all cars that are currently marked as available.
     *
     * @return list of available {@link Car} entities
     */
    List<Car> findByAvailableTrue();

    /**
     * Retrieves cars that are available for booking within the specified date range.
     * <p>
     * A car is considered available if it is marked available and has no overlapping bookings
     * for the given period.
     *
     * @param from start date of the desired booking period (inclusive)
     * @param to   end date of the desired booking period (inclusive)
     * @return list of {@link Car} entities available in the given date range
     */
    @Query("""
       SELECT c FROM Car c
       WHERE c.available = true
         AND NOT EXISTS (
           SELECT 1 FROM Booking b
           WHERE b.carRented.id = c.id
             AND b.startDate <= :to
             AND b.endDate   >= :from
       )
    """)
    List<Car> findAvailableBetween(@Param("from") LocalDate from,
                                   @Param("to")   LocalDate to);

}
