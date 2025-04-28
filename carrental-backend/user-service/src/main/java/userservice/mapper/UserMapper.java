package userservice.mapper;

import userservice.dto.CreateUserRequestDto;
import userservice.dto.UserDto;
import userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting between User entities and their DTO representations.
 * <p>
 * Uses MapStruct to generate implementations at compile time.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a User entity to its DTO representation.
     *
     * @param user the User entity to convert
     * @return a UserDto containing the user’s data
     */
    UserDto toDto(User user);

    /**
     * Converts a UserDto into a User entity.
     * <p>
     * All fields from the DTO are mapped directly to the entity.
     *
     * @param userDto the UserDto to convert
     * @return a User entity populated with the DTO’s data
     */
    User toEntity(UserDto userDto);

    /**
     * Converts a list of User entities into a list of UserDto objects.
     *
     * @param users the list of User entities to convert
     * @return a list of UserDto instances
     */
    List<UserDto> toDtoList(List<User> users);

    /**
     * Creates a new User entity from a CreateUserRequestDto.
     * <p>
     * The entity ID and password fields are ignored and must be set manually in the service.
     *
     * @param dto the CreateUserRequestDto containing user creation data
     * @return a new User entity populated with the DTO’s data (excluding ID and password)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userRole", expression = "java(userservice.model.UserRole.valueOf(dto.userRole()))")
    User toEntity(CreateUserRequestDto dto);
}
