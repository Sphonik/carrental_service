package carbookingservice.dto;

import java.io.Serializable;

public record UserDto(
        String id,
        String firstName,
        String lastName,
        String username,
        String userRole
) implements Serializable {}
