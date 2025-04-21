// src/main/java/com/carrental/service/UserService.java
package com.carrental.service;

import com.carrental.dto.CreateUserRequestDto;
import com.carrental.dto.UpdateUserRequestDto;
import com.carrental.dto.UserDto;
import com.carrental.exception.EntityNotFoundException;
import com.carrental.exception.UsernameAlreadyExistsException;
import com.carrental.mapper.UserMapper;
import com.carrental.model.User;
import com.carrental.model.UserRole;
import com.carrental.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository  userRepository;
    private final UserMapper      userMapper;
    private final PasswordEncoder pwEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder pwEncoder) {
        this.userRepository = userRepository;
        this.userMapper     = userMapper;
        this.pwEncoder      = pwEncoder;
    }

    public List<UserDto> getAllUserDtos() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto getUserDto(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        return userMapper.toDto(user);
    }

    public UserDto createUser(CreateUserRequestDto req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new UsernameAlreadyExistsException(req.username());
        }
        User entity = new User(
                req.firstName(),
                req.lastName(),
                req.username(),
                pwEncoder.encode(req.password()),
                UserRole.valueOf(req.userRole())
        );
        User saved = userRepository.save(entity);
        return userMapper.toDto(saved);
    }

    public UserDto updateUser(Integer id, UpdateUserRequestDto req) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));

        existing.setFirstName(req.firstName());
        existing.setLastName(req.lastName());
        existing.setUsername(req.username());
        if (req.password() != null && !req.password().isBlank()) {
            existing.setPassword(pwEncoder.encode(req.password()));
        }
        existing.setUserRole(UserRole.valueOf(req.userRole()));

        User updated = userRepository.save(existing);
        return userMapper.toDto(updated);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
