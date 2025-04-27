package com.carrental.exception;

/**
 * Exception thrown when a booking request is invalid.
 * <p>
 * Represents errors such as missing required fields or invalid date ranges.
 */
public class InvalidBookingRequestException extends RuntimeException {

    /**
     * Constructs a new InvalidBookingRequestException with the specified detail message.
     *
     * @param message the detail message explaining why the booking request is invalid
     */
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
