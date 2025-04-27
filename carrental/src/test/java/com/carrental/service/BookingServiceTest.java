/*
package com.carrental.service;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.exception.*;
import com.carrental.mapper.BookingMapper;
import com.carrental.model.Booking;
import com.carrental.model.Car;
import com.carrental.model.User;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import com.carrental.integration.CurrencyConverterClient;
import com.carrental.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private CarRepository carRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CurrencyConverterClient currencyClient;

    @Mock
    private BookingMapper mapper;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Car car;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        // Set up mock user, car, and booking data
        user = new User("John", "Doe", "johndoe", "password", null);
        car = new Car();
        car.setId(1);
        car.setModel("Toyota Corolla");
        car.setAvailable(true);
        car.setPricePerDay(BigDecimal.valueOf(50));

        booking = new Booking(user, car, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10), BigDecimal.valueOf(50), "USD");
        booking.setId(1);

        bookingDto = new BookingDto(1, 1, 1, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10), BigDecimal.valueOf(500), "USD");
    }

    @Test
    void testGetAllBookingDtos() {
        // Arrange: Mock repository method
        when(bookingRepo.findAll()).thenReturn(List.of(booking));

        // Arrange: Mock mapper to return BookingDto
        when(mapper.toDtoList(List.of(booking))).thenReturn(List.of(bookingDto));

        // Act: Call the service method
        List<BookingDto> bookingDtos = bookingService.getAllBookingDtos();

        // Assert: Verify the results
        assertNotNull(bookingDtos, "Booking DTOs should not be null");
        assertEquals(1, bookingDtos.size(), "There should be 1 booking DTO");
        assertEquals("USD", bookingDtos.get(0).currency(), "Currency should be 'USD'");
    }

    @Test
    void testGetBookingDto() {
        // Arrange: Mock repository method to return the booking
        when(bookingRepo.findById(1)).thenReturn(Optional.of(booking));

        // Arrange: Mock mapper to return BookingDto
        when(mapper.toDto(booking)).thenReturn(bookingDto);

        // Act: Call the service method
        BookingDto foundBookingDto = bookingService.getBookingDto(1);

        // Assert: Verify the results
        assertNotNull(foundBookingDto, "Booking DTO should not be null");
        assertEquals(1, foundBookingDto.id(), "Booking ID should be 1");
    }

    @Test
    void testCreateBooking() {
        // Arrange: Mock repository methods for user and car existence
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        // Mock booking repo to return the saved booking
        when(bookingRepo.existsOverlapping(car.getId(), booking.getStartDate(), booking.getEndDate())).thenReturn(false);
        when(currencyClient.convert(any(), any())).thenReturn(BigDecimal.valueOf(500)); // Mock currency conversion

        // Mock the mapper to convert the saved booking to BookingDto
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);
        when(mapper.toDto(booking)).thenReturn(bookingDto);

        // Arrange: Prepare the booking request
        BookingRequestDto req = new BookingRequestDto(1, 1L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10), "USD");

        // Act: Call the service method
        BookingDto createdBookingDto = bookingService.createBooking(req);

        // Assert: Verify the results
        assertNotNull(createdBookingDto, "Created booking DTO should not be null");
        assertEquals(500, createdBookingDto.totalCost().intValue(), "Total cost should be 500 USD");
    }

    @Test
    void testCreateBookingCarNotAvailable() {
        // Arrange: Mock repository methods for user and car existence
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));

        // Mock overlapping bookings
        when(bookingRepo.existsOverlapping(car.getId(), booking.getStartDate(), booking.getEndDate())).thenReturn(true);

        // Arrange: Prepare the booking request
        BookingRequestDto req = new BookingRequestDto(1, 1L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10), "USD");

        // Act & Assert: Verify that CarNotAvailableException is thrown
        assertThrows(CarNotAvailableException.class, () -> bookingService.createBooking(req),
                "Car should not be available for the selected dates");
    }

    @Test
    void testDeleteBooking() {
        // Arrange: Mock repository methods to check if the booking exists
        when(bookingRepo.existsById(1)).thenReturn(true);

        // Act: Call the service method
        bookingService.deleteBookingById(1);

        // Assert: Verify that deleteById was called
        verify(bookingRepo, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBookingNotFound() {
        // Arrange: Mock repository method to return false (booking does not exist)
        when(bookingRepo.existsById(99)).thenReturn(false);

        // Act & Assert: Verify that BookingNotFoundException is thrown
        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBookingById(99),
                "Booking with ID 99 does not exist");
    }
}
*/
