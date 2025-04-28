package carbookingservice.exception;

/**
 * Exception thrown when a booking with the specified ID cannot be found.
 */
public class BookingNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookingNotFoundException for the given booking ID.
     *
     * @param id the identifier of the booking that was not found
     */
    public BookingNotFoundException(String id) {
        super("Booking " + id + " is not available");
    }
}
