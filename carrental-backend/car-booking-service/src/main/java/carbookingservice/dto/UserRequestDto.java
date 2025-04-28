package carbookingservice.dto;

import java.io.Serializable;

/**
 * Data transfer object for requesting user information from the User microservice.
 *
 * @param id unique identifier of the user to retrieve
 */
public record UserRequestDto(
        String id
) implements Serializable {}
