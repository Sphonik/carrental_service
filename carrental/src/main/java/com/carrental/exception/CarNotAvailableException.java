package com.carrental.exception;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(Number id) {
        super("Car " + id + " is not available in the requested period");
    }
}

