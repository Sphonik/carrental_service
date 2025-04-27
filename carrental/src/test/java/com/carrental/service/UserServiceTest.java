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
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;  // Mock the UserRepository

    @Mock
    private UserMapper userMapper;  // Mock the UserMapper

    @Mock
    private PasswordEncoder pwEncoder;  // Mock the PasswordEncoder

    @InjectMocks
    private UserService userService;  // Inject the mocks into UserService

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Initialize mock user data
        user = new User("John", "Doe", "johndoe", "password123", UserRole.USER);
        userDto = new UserDto(1, "John", "Doe", "johndoe", "USER");
    }

    @Test
    void testGetAllUserDtos() {
        // Arrange: Mock repository method to return a list of users
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Arrange: Mock the UserMapper to convert User to UserDto
        when(userMapper.toDtoList(List.of(user))).thenReturn(List.of(userDto));

        // Act: Call the service method
        List<UserDto> userDtos = userService.getAllUserDtos();

        // Assert: Verify the results
        assertNotNull(userDtos, "User DTOs should not be null");
        assertEquals(1, userDtos.size(), "There should be 1 user DTO");
        assertEquals("johndoe", userDtos.get(0).username(), "The username should be 'johndoe'");
    }

    @Test
    void testGetUserDto() {
        // Arrange: Mock the repository method to return the user
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Arrange: Mock the UserMapper to convert User to UserDto
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act: Call the service method
        UserDto foundUserDto = userService.getUserDto(1);

        // Assert: Verify the results
        assertNotNull(foundUserDto, "User DTO should not be null");
        assertEquals("johndoe", foundUserDto.username(), "The username should be 'johndoe'");
    }

    @Test
    void testCreateUser() {
        // Arrange: Mock repository method to return false (user does not exist)
        when(userRepository.existsByUsername("johndoe")).thenReturn(false);

        // Arrange: Mock the password encoder
        when(pwEncoder.encode("password123")).thenReturn("encodedPassword");

        // Arrange: Mock the UserMapper to convert User to UserDto
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Arrange: Prepare the CreateUserRequestDto
        CreateUserRequestDto req = new CreateUserRequestDto("John", "Doe", "johndoe", "password123", "USER");

        // Act: Call the service method
        UserDto createdUserDto = userService.createUser(req);

        // Assert: Verify the results
        assertNotNull(createdUserDto, "Created user DTO should not be null");
        assertEquals("johndoe", createdUserDto.username(), "The username should be 'johndoe'");
    }

    @Test
    void testCreateUserUsernameAlreadyExists() {
        // Arrange: Mock repository method to return true (user exists)
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        // Arrange: Prepare the CreateUserRequestDto
        CreateUserRequestDto req = new CreateUserRequestDto("John", "Doe", "johndoe", "password123", "USER");

        // Act & Assert: Verify that UsernameAlreadyExistsException is thrown
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(req),
                "Username 'johndoe' already exists");
    }

    @Test
    void testUpdateUser() {
        // Arrange: Mock the repository method to return an existing user
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Arrange: Mock the UserMapper to convert updated User to UserDto
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Arrange: Prepare the UpdateUserRequestDto
        UpdateUserRequestDto req = new UpdateUserRequestDto("John", "Doe", "johndoe", "newpassword123", "ADMIN");

        // Act: Call the service method
        UserDto updatedUserDto = userService.updateUser(1, req);

        // Assert: Verify the results
        assertNotNull(updatedUserDto, "Updated user DTO should not be null");
        assertEquals("johndoe", updatedUserDto.username(), "The username should be 'johndoe'");
    }

    @Test
    void testDeleteUser() {
        // Arrange: Mock the repository method to return true (user exists)
        when(userRepository.existsById(1)).thenReturn(true);

        // Act: Call the service method
        userService.deleteUser(1);

        // Assert: Verify that the repository method deleteById was called
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        // Arrange: Mock the repository method to return false (user does not exist)
        when(userRepository.existsById(99)).thenReturn(false);

        // Act & Assert: Verify that EntityNotFoundException is thrown
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(99),
                "User with ID 99 does not exist");
    }

    @Test
    void testLogin() {
        // Arrange: Mock repository method to return the user
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act: Call the service method
        Integer userId = userService.login("johndoe", "password123");

        // Assert: Verify the results
        assertNotNull(userId, "User ID should not be null");
        assertEquals(1, userId, "The user ID should be 1");
    }

    @Test
    void testLoginInvalidPassword() {
        // Arrange: Mock repository method to return the user
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        // Act & Assert: Verify that Invalid password exception is thrown
        assertThrows(IllegalArgumentException.class, () -> userService.login("johndoe", "wrongpassword"),
                "Invalid password");
    }
}
