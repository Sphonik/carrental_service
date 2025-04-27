package com.carrental.controller;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing bookings.
 * <p>
 * Provides endpoints to create, retrieve, and delete bookings.
 */
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    /**
     * Constructs a new {@code BookingController} with the given {@link BookingService}.
     *
     * @param bookingService the service responsible for booking operations
     */
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Retrieves all bookings.
     *
     * @return list of all {@link BookingDto}
     */
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

    /**
     * Retrieves all bookings for a specific user.
     *
     * @param userId the ID of the user whose bookings are to be retrieved
     * @return list of {@link BookingDto} for the specified user
     */
    @GetMapping("/user/{userId}")
    public List<BookingDto> getBookingsByUser(@PathVariable Integer userId) {
        return bookingService.getBookingDtosByUser(userId);
    }

    /**
     * Retrieves a single booking by its ID.
     *
     * @param id the ID of the booking to retrieve
     * @return the {@link BookingDto} with the given ID
     */
    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Integer id) {
        return bookingService.getBookingDto(id);
    }

    /**
     * Creates a new booking.
     *
     * @param req the booking request data transfer object
     * @return the created {@link BookingDto}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestBody BookingRequestDto req) {
        return bookingService.createBooking(req);
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id the ID of the booking to delete
     * @return a {@link ResponseEntity} with status 204 (No Content) upon successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable Integer id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }

}
