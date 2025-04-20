package com.carrental.service;

import com.carrental.dto.UserDto;
import com.carrental.exception.EntityNotFoundException;
import com.carrental.mapper.UserMapper;
import com.carrental.model.User;
import com.carrental.model.UserRole;
import com.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /** All users */
    public List<UserDto> getAllUserDtos() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    /** Single user */
    public UserDto getUserDto(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        return userMapper.toDto(user);
    }

    /** Create user */
    public UserDto createUser(UserDto dto) {
        User entity = userMapper.toEntity(dto);
        User saved  = userRepository.save(entity);
        return userMapper.toDto(saved);
    }

    /** Update user */
    public UserDto updateUser(Integer id, UserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setUsername(dto.username());
        existing.setUserRole(UserRole.valueOf(dto.userRole()));

        return userMapper.toDto(userRepository.save(existing));
    }

    /** Delete user */
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
