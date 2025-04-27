package com.carrental.service;

import com.carrental.dto.AvailableCarDto;
import com.carrental.dto.CarDto;
import com.carrental.exception.CarNotAvailableException;
import com.carrental.exception.EntityNotFoundException;
import com.carrental.exception.InvalidBookingRequestException;
import com.carrental.integration.CurrencyConverterClient;
import com.carrental.mapper.CarMapper;
import com.carrental.model.Car;
import com.carrental.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing car operations.
 * <p>
 * Provides methods to list available cars, check availability within a date range,
 * book and return cars, and perform currency conversion for pricing.
 */
@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CurrencyConverterClient currencyConverterClient;

    /**
     * Constructs a new CarService with the specified dependencies.
     *
     * @param carRepository           repository for accessing car entities
     * @param carMapper               mapper for converting between entities and DTOs
     * @param currencyConverterClient client for converting currency values
     */
    public CarService(CarRepository carRepository,
                      CarMapper carMapper,
                      CurrencyConverterClient currencyConverterClient) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.currencyConverterClient = currencyConverterClient;
    }

    /**
     * Retrieves cars available for booking within the specified date range,
     * converting the daily price to the requested currency.
     *
     * @param from     start date of the desired rental period (inclusive)
     * @param to       end date of the desired rental period (inclusive)
     * @param currency ISO currency code for price conversion (e.g., "EUR", "USD")
     * @return list of {@link AvailableCarDto} for cars available in the given date range
     * @throws InvalidBookingRequestException if {@code from} is after {@code to}
     */
    public List<AvailableCarDto> getAvailableBetween(LocalDate from, LocalDate to, String currency) {
        if (from.isAfter(to)) {
            throw new InvalidBookingRequestException("from must be on or before to");
        }

        List<Car> cars = carRepository.findAvailableBetween(from, to);
        return cars.stream()
                .map(car -> {
                    BigDecimal convertedPrice = currencyConverterClient.convert(car.getPricePerDay(), currency);
                    return toAvailableDto(car, convertedPrice, currency);
                })
                .toList();
    }

    /**
     * Retrieves all cars that are currently marked as available.
     *
     * @return list of {@link CarDto} representing available cars
     */
    public List<CarDto> getAvailableCarDtos() {
        return carMapper.toDtoList(carRepository.findByAvailableTrue());
    }

    /**
     * Books a car with the specified ID.
     * <p>
     * Marks the car as rented if it is currently available.
     *
     * @param id the ID of the car to book
     * @return the booked {@link CarDto}
     * @throws EntityNotFoundException    if no car exists with the given ID
     * @throws CarNotAvailableException   if the car is already rented
     */
    public CarDto bookCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));

        if (!car.isAvailable()) {
            throw new CarNotAvailableException(id);
        }

        car.setAvailable(false);
        return carMapper.toDto(carRepository.save(car));
    }

    /**
     * Returns a car with the specified ID.
     * <p>
     * Marks the car as available if it is currently rented.
     *
     * @param id the ID of the car to return
     * @return the returned {@link CarDto}
     * @throws EntityNotFoundException    if no car exists with the given ID
     * @throws CarNotAvailableException   if the car is already available
     */
    public CarDto returnCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));

        if (car.isAvailable()) {
            throw new CarNotAvailableException(id);
        }

        car.setAvailable(true);
        return carMapper.toDto(carRepository.save(car));
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param id the ID of the car to retrieve
     * @return the corresponding {@link CarDto}
     * @throws IllegalArgumentException if no car exists with the given ID
     */
    public CarDto getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with ID " + id + " does not exist"));

        return new CarDto(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getColor(),
                car.getFuelType().toString(),
                car.isAutomatic(),
                car.getPricePerDay(),
                car.getPickupLocation(),
                car.isAvailable()
        );
    }

    /**
     * Helper method to convert a {@link Car} entity into an {@link AvailableCarDto}.
     *
     * @param car             the Car entity
     * @param convertedPrice  the daily price converted to the requested currency
     * @param currency        ISO currency code for the converted price
     * @return the constructed AvailableCarDto
     */
    private AvailableCarDto toAvailableDto(Car car, BigDecimal convertedPrice, String currency) {
        return new AvailableCarDto(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getPricePerDay(),
                convertedPrice.setScale(2, RoundingMode.HALF_UP),
                currency.toUpperCase(),
                car.getYear(),
                car.getColor(),
                car.getFuelType().toString(),
                car.isAutomatic(),
                car.getPickupLocation()
        );
    }
}
