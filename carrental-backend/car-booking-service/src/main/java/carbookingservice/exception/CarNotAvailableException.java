package carbookingservice.exception;

/**
 * Exception thrown when a car with the specified ID is not available
 * for booking in the requested period.
 */
public class CarNotAvailableException extends RuntimeException {

    /**
     * Constructs a new CarNotAvailableException for the given car ID.
     *
     * @param id the identifier of the car that is unavailable
     */
    public CarNotAvailableException(String id) {
        super("Car " + id + " is not available in the requested period");
    }
}
