package com.carrental.mapper;


import com.carrental.dto.UserDto;
import com.carrental.model.Booking;
import com.carrental.model.User;
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
    User toEntity(com.carrental.dto.CreateUserRequestDto dto);
}


