package com.carrental.exception;

/**
 * Exception thrown when attempting to register a user with a username
 * that already exists.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UsernameAlreadyExistsException with a detail message
     * indicating the conflicting username.
     *
     * @param username the username that already exists
     */
    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' already exists");
    }
}
