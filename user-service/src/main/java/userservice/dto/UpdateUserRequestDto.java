package userservice.dto;

public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,    // oder null, wenn Passwort-Update nicht erlaubt
        String userRole
) {}
