package com.carrental.controller;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.carrental.service.BookingService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDto> getAllBookings() {
        logger.info("Fetching all bookings");
        List<BookingDto> bookings = bookingService.getAllBookingDtos();
        if (bookings.isEmpty()) {
            logger.warn("No bookings found in the database");
        } else {
            logger.debug("Found {} bookings", bookings.size());
        }
        return bookings;
    }

    @GetMapping("/user/{userId}")
    public List<BookingDto> byUser(@PathVariable Integer userId) {
        return bookingService.getBookingDtosByUser(userId);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Integer id) {
        return bookingService.getBookingDto(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestBody BookingRequestDto req) {
        return bookingService.createBooking(req);
    }
}

