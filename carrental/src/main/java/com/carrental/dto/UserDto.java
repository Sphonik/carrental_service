package com.carrental.dto;

public record UserDto(
        String id,
        String firstName,
        String lastName,
        String username,
        String userRole
) {}
