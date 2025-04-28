package userservice.controller;

import userservice.dto.CreateUserRequestDto;
import userservice.dto.LoginRequestDto;
import userservice.dto.LoginResponseDto;
import userservice.dto.UpdateUserRequestDto;
import userservice.dto.UserDto;
import userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for managing users.
 * <p>
 * Exposes endpoints to create, retrieve, update, delete, and authenticate users.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the given UserService.
     *
     * @param userService service handling user business logic
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return list of UserDto objects
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUserDtos();
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param id the identifier of the user
     * @return the UserDto corresponding to the given ID
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable String id) {
        return userService.getUserDto(id);
    }

    /**
     * Creates a new user.
     *
     * @param req the DTO containing user creation data
     * @return the created UserDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody CreateUserRequestDto req) {
        return userService.createUser(req);
    }

    /**
     * Updates an existing user.
     *
     * @param id  the identifier of the user to update
     * @param req the DTO containing updated user data
     * @return the updated UserDto
     */
    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable String id,
            @RequestBody UpdateUserRequestDto req
    ) {
        return userService.updateUser(id, req);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the identifier of the user to delete
     * @return ResponseEntity with HTTP 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Authenticates a user with username and password.
     *
     * @param loginRequest DTO containing username and password
     * @return LoginResponseDto containing the authenticated user’s ID
     * @throws ResponseStatusException with status 401 if credentials are invalid
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequest) {
        try {
            String userId = userService.login(
                    loginRequest.username(),
                    loginRequest.password()
            );
            return new LoginResponseDto(userId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }
    }
}
