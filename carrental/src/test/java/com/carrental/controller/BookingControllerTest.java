package com.carrental.controller;

import com.carrental.controller.BookingController;
import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.service.BookingService;
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

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void getAllBookings() throws Exception {
        List<BookingDto> bookingDtos = Arrays.asList(
                new BookingDto(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(3), new BigDecimal("300"), "USD"),
                new BookingDto(2, 2, 2, LocalDate.now(), LocalDate.now().plusDays(5), new BigDecimal("500"), "USD")
        );
        when(bookingService.getAllBookingDtos()).thenReturn(bookingDtos);

        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[1].totalCost").value(500));

        verify(bookingService, times(1)).getAllBookingDtos();
    }

    @Test
    void createBooking() throws Exception {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(3), new BigDecimal("300"), "USD");
        when(bookingService.createBooking(any())).thenReturn(bookingDto);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType("application/json")
                        .content("{\"userId\": 1, \"carId\": 1, \"startDate\": \"2025-05-01\", \"endDate\": \"2025-05-04\", \"totalCost\": 300, \"currency\": \"USD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalCost").value(300));

        verify(bookingService, times(1)).createBooking(any());
    }

    @Test
    void deleteBookingById() throws Exception {
        doNothing().when(bookingService).deleteBookingById(1);

        mockMvc.perform(delete("/api/v1/bookings/1"))
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).deleteBookingById(1);
    }
}
