package com.carrental.mapper;

import com.carrental.dto.CreateUserRequestDto;
import com.carrental.dto.UserDto;
import com.carrental.model.User;
import com.carrental.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting between {@link User} entities and DTOs.
 * <p>
 * Defines methods for mapping:
 * <ul>
 *   <li>{@link User} → {@link UserDto}</li>
 *   <li>{@link UserDto} → {@link User}</li>
 *   <li>Lists of {@link User} ↔ Lists of {@link UserDto}</li>
 *   <li>{@link CreateUserRequestDto} → {@link User}</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     *
     * @param user the User entity to map
     * @return the mapped UserDto
     */
    UserDto toDto(User user);

    /**
     * Converts a {@link UserDto} to a {@link User} entity.
     *
     * @param userDto the UserDto to map
     * @return the mapped User entity
     */
    User toEntity(UserDto userDto);

    /**
     * Converts a list of {@link User} entities to a list of {@link UserDto} objects.
     *
     * @param users the list of User entities to map
     * @return the list of mapped UserDto objects
     */
    List<UserDto> toDtoList(List<User> users);

    /**
     * Creates a new {@link User} entity from a {@link CreateUserRequestDto}.
     * <p>
     * The {@code id} and {@code password} fields are ignored during mapping
     * and should be set manually in the service layer. The {@code userRole}
     * string is converted to the corresponding {@link UserRole} enum value.
     *
     * @param dto the CreateUserRequestDto containing user registration data
     * @return the new User entity (with no ID or encoded password)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userRole", expression = "java(UserRole.valueOf(dto.userRole()))")
    User toEntity(CreateUserRequestDto dto);
}
