package userservice.dto;

/**
 * Data transfer object for user authentication requests.
 *
 * @param username the login name of the user
 * @param password the plaintext password of the user
 */
public record LoginRequestDto(
        String username,
        String password
) {}
