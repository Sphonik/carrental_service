package carbookingservice.exception;

/**
 * Exception thrown when a currency conversion operation fails.
 */
public class CurrencyConversionException extends RuntimeException {

    /**
     * Constructs a new CurrencyConversionException with the specified detail message.
     *
     * @param message the detail message explaining the conversion failure
     */
    public CurrencyConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a new CurrencyConversionException with the specified detail message
     * and cause.
     *
     * @param message the detail message explaining the conversion failure
     * @param cause   the underlying cause of the failure
     */
    public CurrencyConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
