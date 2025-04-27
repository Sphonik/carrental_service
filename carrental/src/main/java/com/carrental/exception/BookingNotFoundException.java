package com.carrental.exception;

/**
 * Exception thrown when a booking with the specified ID cannot be found.
 */
public class BookingNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookingNotFoundException with a detail message
     * indicating the missing booking ID.
     *
     * @param id the ID of the booking that was not found
     */
    public BookingNotFoundException(Number id) {
        super("Booking " + id + " is not available");
    }
}
