package carbookingservice.exception;

/**
 * Exception thrown when a booking request is invalid,
 * such as missing required fields or invalid date ranges.
 */
public class InvalidBookingRequestException extends RuntimeException {

    /**
     * Constructs a new InvalidBookingRequestException with the specified detail message.
     *
     * @param message detailed message describing why the booking request is invalid
     */
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
