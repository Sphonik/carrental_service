// src/main/java/com/carservice/controller/UserController.java
package carservice.controller;

import carservice.dto.*;
import carservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUserDtos();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable String id) {
        return userService.getUserDto(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody CreateUserRequestDto req) {
        return userService.createUser(req);
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable String id,
            @RequestBody UpdateUserRequestDto req
    ) {
        return userService.updateUser(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequest) {
        try {
            // Verwendet den Benutzernamen und das Passwort für die Authentifizierung
            String userId = userService.login(loginRequest.username(), loginRequest.password());

            // Rückgabe der userId bei Erfolg
            return new LoginResponseDto(userId);
        } catch (IllegalArgumentException e) {
            // Gibt 401 (Unauthorized) zurück, wenn die Anmeldedaten ungültig sind
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

}
