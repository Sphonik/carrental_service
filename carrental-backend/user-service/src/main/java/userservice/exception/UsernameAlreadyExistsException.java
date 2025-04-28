package userservice.exception;

/**
 * Exception thrown when attempting to create a user with a username
 * that is already in use.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new exception for the specified username.
     *
     * @param username the username that is already taken
     */
    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' already exists");
    }
}
