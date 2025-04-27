package com.carrental.controller;

import com.carrental.dto.*;
import com.carrental.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for user management.
 * <p>
 * Provides endpoints to create, retrieve, update, delete, and authenticate users.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new {@code UserController} with the given {@link UserService}.
     *
     * @param userService service handling user-related operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return list of {@link UserDto} representing all users
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUserDtos();
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve
     * @return the {@link UserDto} corresponding to the given ID
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUserDto(id);
    }

    /**
     * Creates a new user.
     *
     * @param req the data transfer object containing user creation details
     * @return the created {@link UserDto}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserRequestDto req) {
        return userService.createUser(req);
    }

    /**
     * Updates an existing user.
     *
     * @param id  the ID of the user to update
     * @param req the data transfer object containing updated user details
     * @return the updated {@link UserDto}
     */
    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateUserRequestDto req
    ) {
        return userService.updateUser(id, req);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the ID of the user to delete
     * @return a {@link ResponseEntity} with status 204 (No Content) upon successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Authenticates a user using username and password.
     *
     * @param loginRequest the login request containing username and password
     * @return a {@link LoginResponseDto} containing the authenticated user's ID
     * @throws ResponseStatusException with status 401 (Unauthorized) if credentials are invalid
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequest) {
        try {
            Integer userId = userService.login(loginRequest.username(), loginRequest.password());
            return new LoginResponseDto(userId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

}
