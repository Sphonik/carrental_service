package com.carrental.dto;

/**
 * Data Transfer Object for the login response.
 * <p>
 * Contains the ID of the authenticated user.
 *
 * @param userId unique identifier of the authenticated user
 */
public record LoginResponseDto(Integer userId) {}
