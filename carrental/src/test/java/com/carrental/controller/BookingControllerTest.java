/*
package com.carrental.controller;

import com.carrental.dto.BookingDto;
import com.carrental.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

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
    */
/**
    @Test
    void createBooking() throws Exception {
        BookingDto bookingDto = new BookingDto(
                1, 1, 1,
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 4),
                new BigDecimal("300"), "USD"
        );
        when(bookingService.createBooking(any())).thenReturn(bookingDto);

        String requestJson = objectMapper.writeValueAsString(
                new com.carrental.dto.BookingRequestDto(
                        1, 1L,
                        LocalDate.of(2025, 5, 1),
                        LocalDate.of(2025, 5, 4),
                        "USD"
                )
        );

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
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
    }**//*

}
*/
