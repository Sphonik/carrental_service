package carbookingservice.controller;

import carbookingservice.dto.BookingDto;
import carbookingservice.dto.BookingRequestDto;
import carbookingservice.dto.UserDto;
import carbookingservice.service.BookingService;
import carbookingservice.service.UserEventClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing bookings.
 * <p>
 * Provides endpoints to create, retrieve, and delete bookings,
 * as well as to lookup user information via the UserEventClient.
 */
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final UserEventClient userEventClient;

    /**
     * Constructs a BookingController with the required services.
     *
     * @param bookingService   service layer handling booking operations
     * @param userEventClient  client for retrieving user information from the User microservice
     */
    public BookingController(BookingService bookingService, UserEventClient userEventClient) {
        this.bookingService = bookingService;
        this.userEventClient = userEventClient;
    }

    /**
     * Retrieves all bookings.
     *
     * @return a list of BookingDto representing all existing bookings
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
     * @param userId the identifier of the user whose bookings are requested
     * @return a list of BookingDto for the given user
     */
    @GetMapping("/user/{userId}")
    public List<BookingDto> byUser(@PathVariable String userId) {
        return bookingService.getBookingDtosByUser(userId);
    }

    /**
     * Looks up user details by user ID via the User microservice.
     *
     * @param userId the identifier of the user to lookup
     * @return a UserDto containing the user's information
     */
    @GetMapping("/userlookup/{userId}")
    public UserDto lookup(@PathVariable String userId) {
        return userEventClient.getUserById(userId);
    }

    /**
     * Retrieves a single booking by its booking ID.
     *
     * @param id the identifier of the booking
     * @return the BookingDto corresponding to the given ID
     */
    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable String id) {
        return bookingService.getBookingDto(id);
    }

    /**
     * Creates a new booking based on the provided request data.
     *
     * @param req the BookingRequestDto containing booking details
     * @return the created BookingDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestBody BookingRequestDto req) {
        return bookingService.createBooking(req);
    }

    /**
     * Deletes a booking by its booking ID.
     *
     * @param id the identifier of the booking to delete
     * @return a ResponseEntity with HTTP status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable String id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build();
    }
}
