package com.carrental.dto;

/**
 * Data Transfer Object for creating a new user.
 * <p>
 * Contains personal details and credentials required for user registration.
 *
 * @param firstName first name of the user
 * @param lastName  last name of the user
 * @param username  unique username for login
 * @param password  password for the account (will be encoded)
 * @param userRole  role assigned to the user (e.g., "ADMIN", "USER")
 */
public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String username,
        String password,
        String userRole
) {}
