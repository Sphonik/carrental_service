package userservice.dto;

/**
 * Data transfer object for creating a new user.
 * <p>
 * Contains the user's personal details, login credentials, and assigned role.
 *
 * @param firstName user's first name
 * @param lastName  user's last name
 * @param username  desired login name for the user
 * @param password  plaintext password (will be encoded internally)
 * @param userRole  role to assign to the user (e.g., "ADMIN", "USER", "TESTER")
 */
public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,
        String userRole
) {}
