package userservice.dto;

/**
 * Data transfer object for updating an existing user.
 * <p>
 * Contains fields that can be modified; password may be null if not being updated.
 *
 * @param firstName user's new first name
 * @param lastName  user's new last name
 * @param username  user's new username
 * @param password  new plaintext password, or {@code null} to leave unchanged
 * @param userRole  new role to assign to the user (e.g., "ADMIN", "USER", "TESTER")
 */
public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,
        String userRole
) {}
