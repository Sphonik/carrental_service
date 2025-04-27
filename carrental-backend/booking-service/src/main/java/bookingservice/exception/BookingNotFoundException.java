package bookingservice.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String id) {
        super("Booking " + id + " is not available");
    }
}
