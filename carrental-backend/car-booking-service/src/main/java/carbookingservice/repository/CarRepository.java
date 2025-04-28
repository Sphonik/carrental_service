package carbookingservice.repository;

import carbookingservice.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Car entities in MongoDB.
 * <p>
 * Provides methods to retrieve cars based on availability or by make and model.
 */
@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    /**
     * Retrieves all cars that are currently marked as available.
     *
     * @return a list of available Car entities
     */
    List<Car> findByAvailableTrue();

    /**
     * Finds a car by its make and model.
     *
     * @param make  the manufacturer name of the car
     * @param model the model name of the car
     * @return an Optional containing the Car if found, otherwise empty
     */
    Optional<Car> findByMakeAndModel(String make, String model);

}
