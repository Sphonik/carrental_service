package com.carrental.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Number id) {
        super("Booking " + id + " is not available");
    }
}
