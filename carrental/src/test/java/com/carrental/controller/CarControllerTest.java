package com.carrental.controller;

import com.carrental.controller.CarController;
import com.carrental.dto.CarDto;
import com.carrental.dto.AvailableCarDto;
import com.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void getAvailableCars() throws Exception {
        List<CarDto> carDtos = Arrays.asList(
                new CarDto(1, "Toyota", "Corolla", 2021, "Red", "Gasoline", true, new BigDecimal("50"), "Location1", true),
                new CarDto(2, "Honda", "Civic", 2022, "Blue", "Gasoline", false, new BigDecimal("60"), "Location2", true)
        );
        when(carService.getAvailableCarDtos()).thenReturn(carDtos);

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[1].model").value("Civic"));

        verify(carService, times(1)).getAvailableCarDtos();
    }

    @Test
    void bookCar() throws Exception {
        CarDto carDto = new CarDto(1, "Toyota", "Corolla", 2021, "Red", "Gasoline", true, new BigDecimal("50"), "Location1", true);
        when(carService.bookCar(1L)).thenReturn(carDto);

        mockMvc.perform(post("/api/v1/cars/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(carService, times(1)).bookCar(1L);
    }

    @Test
    void returnCar() throws Exception {
        CarDto carDto = new CarDto(1, "Toyota", "Corolla", 2021, "Red", "Gasoline", true, new BigDecimal("50"), "Location1", true);
        when(carService.returnCar(1L)).thenReturn(carDto);

        mockMvc.perform(post("/api/v1/cars/return/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(carService, times(1)).returnCar(1L);
    }

    @Test
    void getAvailableBetween() throws Exception {
        // Corrected AvailableCarDto with BigDecimal values
        List<AvailableCarDto> availableCarDtos = Arrays.asList(
                new AvailableCarDto(
                        1,
                        "Toyota",
                        "Corolla",
                        new BigDecimal("20.99"),
                        new BigDecimal("21.00"),
                        "USD",  // Add currency
                        2021,    // Add year
                        "Red",   // Add color
                        "Gasoline", // Add fuel type
                        true,    // Add automatic (boolean)
                        "Location1"  // Add pickup location
                ),
                new AvailableCarDto(
                        2,
                        "Honda",
                        "Civic",
                        new BigDecimal("20.99"),
                        new BigDecimal("22.00"),
                        "USD",  // Add currency
                        2022,    // Add year
                        "Blue",  // Add color
                        "Gasoline",  // Add fuel type
                        false,   // Add automatic (boolean)
                        "Location2"  // Add pickup location
                )
        );
        when(carService.getAvailableBetween(any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(availableCarDtos);

        mockMvc.perform(get("/api/v1/cars/available")
                        .param("from", "2025-01-01")
                        .param("to", "2025-01-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(carService, times(1)).getAvailableBetween(any(LocalDate.class), any(LocalDate.class), anyString());
    }
}
