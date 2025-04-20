package com.carrental.repository;


import com.carrental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
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
