package com.carrental.mapper;


import com.carrental.dto.UserDto;
import com.carrental.model.Booking;
import com.carrental.model.User;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    List<UserDto> toDtoList(List<User> users);
}


