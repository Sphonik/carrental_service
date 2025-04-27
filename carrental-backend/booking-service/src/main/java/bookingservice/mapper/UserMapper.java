package bookingservice.mapper;


import bookingservice.dto.CreateUserRequestDto;
import bookingservice.dto.UserDto;
import bookingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    List<UserDto> toDtoList(List<User> users);
    // Erzeugt ein User‐Entity aus CreateUserRequestDto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // wird manuell im Service encodet
    @Mapping(target = "userRole", expression = "java(UserRole.valueOf(dto.userRole()))")
    User toEntity(CreateUserRequestDto dto);
}


