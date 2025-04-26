package carservice.dto;

public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,
        String userRole
) {}
