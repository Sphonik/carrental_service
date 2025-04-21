// src/main/java/com/carrental/controller/UserController.java
package com.carrental.controller;

import com.carrental.dto.CreateUserRequestDto;
import com.carrental.dto.UpdateUserRequestDto;
import com.carrental.dto.UserDto;
import com.carrental.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public UserDto getUser(@PathVariable Integer id) {
        return userService.getUserDto(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody CreateUserRequestDto req) {
        return userService.createUser(req);
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable Integer id,
            @RequestBody UpdateUserRequestDto req
    ) {
        return userService.updateUser(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
