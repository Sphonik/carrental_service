package carbookingservice.dto;

import java.io.Serializable;

/**
 * Data transfer object representing user details provided by the User microservice.
 *
 * @param id         unique identifier of the user
 * @param firstName  user's first name
 * @param lastName   user's last name
 * @param username   login name of the user
 * @param userRole   role assigned to the user (e.g., "ADMIN", "USER")
 */
public record UserDto(
        String id,
        String firstName,
        String lastName,
        String username,
        String userRole
) implements Serializable {}
