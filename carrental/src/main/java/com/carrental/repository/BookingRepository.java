package com.carrental.repository;

import com.carrental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for {@link Booking} entities.
 * <p>
 * Provides methods to perform CRUD operations and custom queries on bookings.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    /**
     * Finds all bookings made by a specific user.
     *
     * @param userId the ID of the user
     * @return list of {@link Booking} entities for the given user
     */
    List<Booking> findByBookedBy_Id(Integer userId);

    /**
     * Checks if there exists any booking for a given car that overlaps
     * with the specified date range.
     *
     * @param carId     the ID of the car
     * @param startDate the start date of the new booking period
     * @param endDate   the end date of the new booking period
     * @return {@code true} if an overlapping booking exists, {@code false} otherwise
     */
    @Query("""
            SELECT COUNT(b) > 0
              FROM Booking b
             WHERE b.carRented.id = :carId
               AND b.startDate   <= :endDate
               AND b.endDate     >= :startDate
            """)
    boolean existsOverlapping(
            @Param("carId")     Integer carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate")   LocalDate endDate);
}
