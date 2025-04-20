package com.carrental.dto;

public record UserDto(
        Integer id,
        String firstName,
        String lastName,
        String username,
        String userRole
) {}
