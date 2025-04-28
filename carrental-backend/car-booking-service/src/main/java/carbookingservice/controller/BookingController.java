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

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final UserEventClient userEventClient;

    public BookingController(BookingService bookingService, UserEventClient userEventClient) {
        this.bookingService = bookingService;
        this.userEventClient = userEventClient;
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
    public List<BookingDto> byUser(@PathVariable String userId) {
        return bookingService.getBookingDtosByUser(userId);
    }

    @GetMapping("/userlookup/{userId}")
    public UserDto lookup(@PathVariable String userId) {
        return userEventClient.getUserById(userId);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable String id) {
        return bookingService.getBookingDto(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestBody BookingRequestDto req) {
        return bookingService.createBooking(req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable String id) {
        bookingService.deleteBookingById(id);
        return ResponseEntity.noContent().build(); // Antwort: 204 No Content
    }


}

