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

/**
 * Service for managing user accounts.
 * <p>
 * Provides methods to retrieve, create, update, delete, and authenticate users.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder pwEncoder;

    /**
     * Constructs a new UserService with the specified dependencies.
     *
     * @param userRepository repository for User entities
     * @param userMapper     mapper for converting between User entities and DTOs
     * @param pwEncoder      encoder for hashing user passwords
     */
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder pwEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pwEncoder = pwEncoder;
    }

    /**
     * Retrieves all users as DTOs.
     *
     * @return list of all {@link UserDto}
     */
    public List<UserDto> getAllUserDtos() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve
     * @return the corresponding {@link UserDto}
     * @throws EntityNotFoundException if no user exists with the given ID
     */
    public UserDto getUserDto(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        return userMapper.toDto(user);
    }

    /**
     * Creates a new user account.
     *
     * @param req the {@link CreateUserRequestDto} containing registration details
     * @return the created {@link UserDto}
     * @throws UsernameAlreadyExistsException if the username is already in use
     */
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

    /**
     * Updates an existing user account.
     *
     * @param id  the ID of the user to update
     * @param req the {@link UpdateUserRequestDto} containing updated user details
     * @return the updated {@link UserDto}
     * @throws EntityNotFoundException if no user exists with the given ID
     */
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

    /**
     * Deletes a user account by its ID.
     *
     * @param id the ID of the user to delete
     * @throws EntityNotFoundException if no user exists with the given ID
     */
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username the username to authenticate
     * @param password the raw password to verify
     * @return the authenticated user's ID
     * @throws IllegalArgumentException if the user is not found or the password is invalid
     */
    public Integer login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!pwEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user.getId();
    }
}
