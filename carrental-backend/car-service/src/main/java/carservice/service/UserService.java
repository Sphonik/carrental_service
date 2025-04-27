// src/main/java/com/carrental/service/UserService.java
package carservice.service;

import carservice.dto.CreateUserRequestDto;
import carservice.dto.UpdateUserRequestDto;
import carservice.dto.UserDto;
import carservice.exception.EntityNotFoundException;
import carservice.exception.UsernameAlreadyExistsException;
import carservice.mapper.UserMapper;
import carservice.model.User;
import carservice.model.UserRole;
import carservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository  userRepository;
    private final UserMapper      userMapper;
    private final PasswordEncoder encoder;   // hier: NoOpPasswordEncoder

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper     = userMapper;
        this.encoder        = encoder;
    }

    /* ---------- CRUD ---------- */

    public List<UserDto> getAllUserDtos() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDto getUserDto(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User", id));
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
                encoder.encode(req.password()),          // Klartext bei NoOp
                UserRole.valueOf(req.userRole())
        );
        return userMapper.toDto(userRepository.save(entity));
    }

    public UserDto updateUser(String id, UpdateUserRequestDto req) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User", id));

        existing.setFirstName(req.firstName());
        existing.setLastName (req.lastName());
        existing.setUsername (req.username());

        if (req.password() != null && !req.password().isBlank()) {
            existing.setPassword(encoder.encode(req.password()));
        }
        existing.setUserRole(UserRole.valueOf(req.userRole()));

        return userMapper.toDto(userRepository.save(existing));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    /* ---------- Login ---------- */

    public String login(String username, String rawPassword) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);

        if (!encoder.matches(rawPassword, u.getPassword())) {   // Identity-Vergleich
            throw new IllegalArgumentException();
        }
        return u.getId();
    }
}
