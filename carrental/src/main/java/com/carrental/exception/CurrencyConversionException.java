package com.carrental.exception;

/**
 * Exception thrown when an error occurs during currency conversion.
 */
public class CurrencyConversionException extends RuntimeException {

    /**
     * Constructs a new CurrencyConversionException with the specified detail message.
     *
     * @param message the detail message explaining the conversion error
     */
    public CurrencyConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a new CurrencyConversionException with the specified detail message and cause.
     *
     * @param message the detail message explaining the conversion error
     * @param cause   the underlying cause of the conversion failure
     */
    public CurrencyConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
