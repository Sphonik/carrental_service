// src/main/java/com/carrental/service/BookingService.java
package carservice.service;

import carservice.dto.BookingDto;
import carservice.dto.BookingRequestDto;
import carservice.exception.*;
import carservice.integration.CurrencyConverterClient;
import carservice.mapper.BookingMapper;
import carservice.model.Booking;
import carservice.model.Car;
import carservice.model.User;
import carservice.repository.BookingRepository;
import carservice.repository.CarRepository;
import carservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository      bookingRepo;
    private final BookingMapper          mapper;
    private final CarRepository          carRepo;
    private final UserRepository         userRepo;
    private final CurrencyConverterClient currencyClient;

    public BookingService(BookingRepository bookingRepo,
                          BookingMapper mapper,
                          CarRepository carRepo,
                          UserRepository userRepo,
                          CurrencyConverterClient currencyClient) {
        this.bookingRepo    = bookingRepo;
        this.mapper         = mapper;
        this.carRepo        = carRepo;
        this.userRepo       = userRepo;
        this.currencyClient = currencyClient;
    }

    public List<BookingDto> getAllBookingDtos() {
        return mapper.toDtoList(bookingRepo.findAll());
    }

    public BookingDto getBookingDto(String id) {
        Booking entity = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking", id));
        return mapper.toDto(entity);
    }

    public List<BookingDto> getBookingDtosByUser(String userId) {
        return mapper.toDtoList(bookingRepo.findByBookedById(userId));
    }

    public BookingDto createBooking(BookingRequestDto req) {
        if (req.startDate().isAfter(req.endDate())) {
            throw new InvalidBookingRequestException("startDate must be before endDate");
        }

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("User", req.userId()));
        Car  car  = carRepo.findById(req.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car", req.carId()));

        boolean overlapping = bookingRepo.existsByCarRentedIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                car.getId(), req.endDate(), req.startDate());
        if (overlapping) {
            throw new CarNotAvailableException(car.getId());
        }

        long days     = ChronoUnit.DAYS.between(req.startDate(), req.endDate());
        BigDecimal costUsd = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        BigDecimal totalCost;
        try {
            totalCost = currencyClient.convert(costUsd, req.currency());
        } catch (CurrencyConversionException ex) {
            log.warn("Currency conversion failed – falling back to USD", ex);
            totalCost = costUsd;
        }

        Booking entity = new Booking(
                user.getId(),
                car.getId(),
                req.startDate(),
                req.endDate(),
                car.getPricePerDay(),
                req.currency().toUpperCase()
        );
        entity.setTotalCost(totalCost);

        Booking saved = bookingRepo.save(entity);
        log.info("Booked car {} for user {} ({} {})",
                car.getId(), user.getId(), totalCost, req.currency().toUpperCase());

        return mapper.toDto(saved);
    }

    public void deleteBookingById(String id) {
        if (!bookingRepo.existsById(id)) {
            throw new BookingNotFoundException(id);
        }
        bookingRepo.deleteById(id);
    }
}
