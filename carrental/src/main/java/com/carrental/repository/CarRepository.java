package com.carrental.repository;

import java.util.Optional;
import com.carrental.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    /** Alle Cars, die aktuell als verfügbar markiert sind */
    List<Car> findByAvailableTrue();
    Optional<Car> findByMakeAndModel(String make, String model);

}
