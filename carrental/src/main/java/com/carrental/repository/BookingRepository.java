package com.carrental.repository;

import com.carrental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    /* alle Buchungen eines Users */
    List<Booking> findByBookedBy_Id(Integer userId);

    /* true, wenn sich bereits eine Buchung überschneidet */
    @Query("""
            SELECT COUNT(b) > 0
              FROM Booking b
             WHERE b.carRented.id = :carId
               AND b.startDate   <= :endDate
               AND b.endDate     >= :startDate
            """)
    boolean existsOverlapping(@Param("carId")     Integer carId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate")   LocalDate endDate);
}
