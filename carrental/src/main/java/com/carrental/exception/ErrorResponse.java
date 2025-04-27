package com.carrental.exception;

import java.time.Instant;

/**
 * Standard structure for error responses returned by the API.
 * <p>
 * Contains details about the error, including HTTP status and request path.
 *
 * @param timestamp the time at which the error occurred
 * @param status    the HTTP status code (e.g., 404, 500)
 * @param error     the short error description corresponding to the status
 * @param message   detailed error message for troubleshooting
 * @param path      the request path that triggered the error
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}
