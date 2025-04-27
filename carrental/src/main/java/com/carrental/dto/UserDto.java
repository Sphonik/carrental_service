package com.carrental.dto;

/**
 * Data Transfer Object representing a user.
 * <p>
 * Used for transferring user information between layers without exposing entities.
 *
 * @param id         unique identifier of the user
 * @param firstName  first name of the user
 * @param lastName   last name of the user
 * @param username   unique username for login
 * @param userRole   role assigned to the user (e.g., "ADMIN", "USER")
 */
public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String username,
        String userRole
) {}
