package carservice.exception;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(String id) {
        super("Car " + id + " is not available in the requested period");
    }
}

