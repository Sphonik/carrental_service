package com.carrental.exception;

/**
 * Exception thrown when a car with the specified ID is not available
 * for booking in the requested period.
 */
public class CarNotAvailableException extends RuntimeException {

    /**
     * Constructs a new CarNotAvailableException with a detail message
     * indicating the unavailable car ID.
     *
     * @param id the ID of the car that is unavailable
     */
    public CarNotAvailableException(Number id) {
        super("Car " + id + " is not available in the requested period");
    }
}
