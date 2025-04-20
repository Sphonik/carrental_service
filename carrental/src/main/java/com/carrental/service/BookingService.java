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

@Service
@Transactional
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository      bookingRepo;
    private final BookingMapper          mapper;
    private final CarRepository          carRepo;
    private final UserRepository         userRepo;
    private final CurrencyConverterClient currencyClient;

    /* ----------  expliziter Konstruktor (kein Lombok) ---------- */
    public BookingService(BookingRepository bookingRepo,
                          BookingMapper mapper,
                          CarRepository carRepo,
                          UserRepository userRepo,
                          CurrencyConverterClient currencyClient) {
        this.bookingRepo   = bookingRepo;
        this.mapper        = mapper;
        this.carRepo       = carRepo;
        this.userRepo      = userRepo;
        this.currencyClient= currencyClient;
    }
    /* ----------------------------------------------------------- */

    /* ---------------------  READ  ------------------------------ */

    public List<BookingDto> getAllBookingDtos() {
        return mapper.toDtoList(bookingRepo.findAll());
    }

    public BookingDto getBookingDto(Integer id) {
        Booking entity = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking", id));
        return mapper.toDto(entity);
    }

    public List<BookingDto> getBookingDtosByUser(Integer userId) {
        return mapper.toDtoList(bookingRepo.findByBookedBy_Id(userId));
    }

    /* --------------------  CREATE  ----------------------------- */

    public BookingDto createBooking(BookingRequestDto req) {

        /* 1) Grundvaliderung der Daten */
        if (req.startDate().isAfter(req.endDate())) {
            throw new InvalidBookingRequestException("startDate must be before endDate");
        }

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("User", req.userId()));
        Car  car  = carRepo.findById(req.carId())
                .orElseThrow(() -> new EntityNotFoundException("Car", req.carId()));

        /* 2) Verfügbarkeit */
        boolean overlapping = bookingRepo.existsOverlapping(
                car.getId(), req.startDate(), req.endDate());
        if (overlapping) {
            throw new CarNotAvailableException(car.getId());
        }

        /* 3) Kosten berechnen – Basis = USD */
        long        days        = ChronoUnit.DAYS.between(req.startDate(), req.endDate());
        BigDecimal  costUsd     = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        /* 4) Währungs­konvertierung */
        BigDecimal totalCost;
        try {
            totalCost = currencyClient.convert(costUsd, req.currency());
        } catch (CurrencyConversionException ex) {
            log.warn("Currency conversion failed – falling back to USD", ex);
            totalCost = costUsd;
        }

        /* 5) Entität bauen & speichern */
        Booking entity = new Booking(user, car,
                req.startDate(), req.endDate(),
                car.getPricePerDay(), req.currency().toUpperCase());
        entity.setTotalCost(totalCost);          // überschreibt USD‑Kosten mit Ziel­währung

        Booking saved = bookingRepo.save(entity);
        log.info("Booked car {} for user {} ({} {})",
                car.getId(), user.getId(), totalCost, req.currency().toUpperCase());

        return mapper.toDto(saved);
    }
}
