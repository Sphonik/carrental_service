package com.carrental.dto;

/**
 * Data Transfer Object for updating an existing user.
 * <p>
 * Contains the details to update for a user. The password field may be null
 * if a password update is not permitted.
 *
 * @param firstName first name of the user
 * @param lastName  last name of the user
 * @param username  unique username for login
 * @param password  new password for the account, or null if password update is not allowed
 * @param userRole  role assigned to the user (e.g., "ADMIN", "USER")
 */
public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,
        String userRole
) {}
