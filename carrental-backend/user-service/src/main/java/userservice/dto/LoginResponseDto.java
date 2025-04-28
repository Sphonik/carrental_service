package userservice.dto;

/**
 * Data transfer object returned after successful user authentication.
 *
 * @param userId the unique identifier of the authenticated user
 */
public record LoginResponseDto(
        String userId
) {}
