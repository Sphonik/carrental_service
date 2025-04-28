package userservice.exception;

import java.time.Instant;

/**
 * Data transfer object representing a standardized error response payload
 * for the User service.
 *
 * @param timestamp the moment when the error occurred
 * @param status    the HTTP status code returned to the client
 * @param error     the HTTP status reason phrase
 * @param message   a detailed message describing the error
 * @param path      the request path that caused the error
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}
