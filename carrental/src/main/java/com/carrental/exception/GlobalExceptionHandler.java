package com.carrental.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global exception handler that intercepts and processes exceptions thrown by controllers,
 * mapping them to standardized {@link ErrorResponse} payloads.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@link EntityNotFoundException}.
     *
     * @param ex      the exception indicating the missing entity
     * @param request the HTTP request during which the exception was raised
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with status 404 (Not Found)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles exceptions of type {@link UsernameAlreadyExistsException}.
     *
     * @param ex      the exception indicating a username conflict
     * @param request the HTTP request during which the exception was raised
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with status 409 (Conflict)
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameConflict(
            UsernameAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Handles exceptions of type {@link CarNotAvailableException}.
     *
     * @param ex      the exception indicating an unavailable car
     * @param request the HTTP request during which the exception was raised
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with status 409 (Conflict)
     */
    @ExceptionHandler(CarNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleCarUnavailable(
            CarNotAvailableException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Handles validation and bad request exceptions.
     *
     * @param ex      the exception indicating invalid request data
     * @param request the HTTP request during which the exception was raised
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with status 400 (Bad Request)
     */
    @ExceptionHandler({InvalidBookingRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles exceptions of type {@link CurrencyConversionException}.
     *
     * @param ex      the exception indicating a failure in currency conversion
     * @param request the HTTP request during which the exception was raised
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with status 503 (Service Unavailable)
     */
    @ExceptionHandler(CurrencyConversionException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyError(
            CurrencyConversionException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Currency service unavailable",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    /**
     * Handles all uncaught exceptions as a fallback.
     *
     * @param ex      the exception that was not otherwise handled
     * @param request the HTTP request during which the exception was raised
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred",
                request.getRequestURI()
        );

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
