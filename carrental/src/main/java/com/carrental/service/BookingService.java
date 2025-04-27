package com.carrental.service;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.exception.*;
import com.carrental.integration.CurrencyConverterClient;
import com.carrental.mapper.BookingMapper;
import com.carrental.model.Booking;
import com.carrental.model.Car;
import com.carrental.model.User;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service for managing bookings.
 * <p>
 * Provides methods to create, retrieve, and delete bookings,
 * including validation, availability checks, and currency conversion.
 */
@Service
@Transactional
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepo;
    private final BookingMapper mapper;
    private final CarRepository carRepo;
    private final UserRepository userRepo;
    private final CurrencyConverterClient currencyClient;

    /**
     * Constructs a BookingService with the required dependencies.
     *
     * @param bookingRepo      repository for booking entities
     * @param mapper           mapper between Booking and BookingDto
     * @param carRepo          repository for car entities
     * @param userRepo         repository for user entities
     * @param currencyClient   client for performing currency conversions
     * @param bookingRepository duplicate parameter for compatibility
     */
    public BookingService(BookingRepository bookingRepo,
                          BookingMapper mapper,
                          CarRepository carRepo,
                          UserRepository userRepo,
                          CurrencyConverterClient currencyClient,
                          BookingRepository bookingRepository) {
        this.bookingRepo = bookingRepo;
        this.mapper = mapper;
        this.carRepo = carRepo;
        this.userRepo = userRepo;
        this.currencyClient = currencyClient;
    }

    /**
     * Retrieves all bookings as DTOs.
     *
     * @return list of BookingDto representing all bookings
     */
    public List<BookingDto> getAllBookingDtos() {
        return mapper.toDtoList(bookingRepo.findAll());
    }

    /**
     * Retrieves a booking by its ID.
     *
     * @param id the ID of the booking
     * @return the BookingDto for the specified ID
     * @throws EntityNotFoundException if no booking exists with the given ID
     */
    public BookingDto getBookingDto(Integer id) {
        Booking entity = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking", id));
        return mapper.toDto(entity);
    }

    /**
     * Retrieves all bookings for a specific user.
     *
     * @param userId the ID of the user
     * @return list of BookingDto for the specified user
     */
    public List<BookingDto> getBookingDtosByUser(Integer userId) {
        return mapper.toDtoList(bookingRepo.findByBookedBy_Id(userId));
    }

    /**
     * Creates a new booking based on the provided request DTO.
     * <p>
     * Steps:
     * <ol>
     *   <li>Validate date range (startDate ≤ endDate)</li>
     *   <li>Ensure user and car exist</li>
     *   <li>Check car availability (no overlapping bookings)</li>
     *   <li>Calculate total cost in USD</li>
     *   <li>Convert to requested currency, fallback to USD on failure</li>
     *   <li>Persist booking entity and return DTO</li>
     * </ol>
     *
     * @param req the BookingRequestDto containing booking details
     * @return the created BookingDto
     * @throws InvalidBookingRequestException if startDate is after endDate
     * @throws EntityNotFoundException        if the user or car cannot be found
     * @throws CarNotAvailableException       if the car is already booked in the specified period
     */
    public BookingDto createBooking(BookingRequestDto req) {
        if (req.startDate().isAfter(req.endDate())) {
            throw new InvalidBookingRequestException("startDate must be before endDate");
        }

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("User", req.userId()));
        Car car = carRepo.findById(req.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car", req.carId()));

        boolean overlapping = bookingRepo.existsOverlapping(
                car.getId(), req.startDate(), req.endDate());
        if (overlapping) {
            throw new CarNotAvailableException(car.getId());
        }

        long days = ChronoUnit.DAYS.between(req.startDate(), req.endDate());
        BigDecimal costUsd = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        BigDecimal totalCost;
        try {
            totalCost = currencyClient.convert(costUsd, req.currency());
        } catch (CurrencyConversionException ex) {
            log.warn("Currency conversion failed – falling back to USD", ex);
            totalCost = costUsd;
        }

        Booking entity = new Booking(
                user, car,
                req.startDate(), req.endDate(),
                car.getPricePerDay(), req.currency().toUpperCase());
        entity.setTotalCost(totalCost);

        Booking saved = bookingRepo.save(entity);
        log.info("Booked car {} for user {}: {} {}",
                car.getId(), user.getId(), totalCost, req.currency().toUpperCase());

        return mapper.toDto(saved);
    }

    /**
     * Deletes a booking by its ID.
     *
     * @param id the ID of the booking to delete
     * @throws BookingNotFoundException if no booking exists with the given ID
     */
    @Transactional
    public void deleteBookingById(Integer id) {
        if (!bookingRepo.existsById(id)) {
            throw new BookingNotFoundException(id);
        }
        bookingRepo.deleteById(id);
    }
}
