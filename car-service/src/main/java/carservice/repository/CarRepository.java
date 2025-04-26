package carservice.repository;

import carservice.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    /** Alle Cars, die aktuell als verfügbar markiert sind */
    List<Car> findByAvailableTrue();
    Optional<Car> findByMakeAndModel(String make, String model);

}
