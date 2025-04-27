package com.carrental.dto;

/**
 * Data Transfer Object for user login credentials.
 * <p>
 * Contains the username and password required for authentication.
 *
 * @param username unique username of the user
 * @param password password of the user
 */
public record LoginRequestDto(String username, String password) {}
