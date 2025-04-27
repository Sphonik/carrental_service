/*
package com.carrental.repository;

import com.carrental.dto.UserDto;
import com.carrental.exception.EntityNotFoundException;
import com.carrental.mapper.UserMapper;
import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;  // Mock the UserRepository

    @Mock
    private UserMapper userMapper;  // Mock the UserMapper

    @InjectMocks
    private UserService userService;  // Inject the mock repository and userMapper into UserService

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Initialize mock user data
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");

        // Initialize mock UserDto data
        userDto = new UserDto(1, "John", "Doe", "testuser", "USER");
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
        assertEquals("testuser", userDtos.get(0).username(), "The username should be 'testuser'");
    }

    @Test
    void testFindUserByIdNotFound() {
        // Arrange: Mock the repository method to return an empty Optional when user is not found
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert: Verify that no user is found
        assertThrows(EntityNotFoundException.class, () -> userService.getUserDto(99),
                "User with ID 99 should not be found");
    }

    @Test
    void testFindUserByUsername() {
        // Arrange: Mock the repository method to return the mock user
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Arrange: Mock the UserMapper to convert User to UserDto
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act: Call the service method
        UserDto foundUserDto = userService.getUserDto(1);

        // Assert: Verify the result
        assertNotNull(foundUserDto, "User DTO should not be null");
        assertEquals("testuser", foundUserDto.username(), "The username should be 'testuser'");
    }


}
*/
