/*
package com.carrental.controller;

import com.carrental.controller.UserController;
import com.carrental.dto.UserDto;
import com.carrental.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserDto> userDtos = Arrays.asList(
                new UserDto(1, "John", "Doe", "johndoe", "USER"),
                new UserDto(2, "Jane", "Doe", "janedoe", "ADMIN")
        );
        when(userService.getAllUserDtos()).thenReturn(userDtos);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[1].userRole").value("ADMIN"));

        verify(userService, times(1)).getAllUserDtos();
    }

    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto(1, "John", "Doe", "johndoe", "USER");
        when(userService.createUser(any())).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"username\":\"johndoe\",\"userRole\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.username").value("johndoe"));

        verify(userService, times(1)).createUser(any());
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }
}
*/
