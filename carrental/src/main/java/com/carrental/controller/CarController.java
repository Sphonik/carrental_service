package com.carrental.controller;

import com.carrental.dto.AvailableCarDto;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import com.carrental.dto.CarDto;
import com.carrental.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for car operations.
 * <p>
 * Provides endpoints to list available cars, book and return cars,
 * and query availability within a date range and currency.
 */
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    /**
     * Constructs a new {@code CarController} with the given {@link CarService}.
     *
     * @param carService service handling car-related business logic
     */
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Retrieves all available cars.
     *
     * @return a list of {@link CarDto} representing available cars
     */
    @GetMapping
    public List<CarDto> getAvailableCars() {
        return carService.getAvailableCarDtos();
    }

    /**
     * Books the car with the specified ID.
     *
     * @param id the ID of the car to book
     * @return a {@link ResponseEntity} containing the booked {@link CarDto}
     */
    @PostMapping("/book/{id}")
    public ResponseEntity<CarDto> bookCar(@PathVariable Long id) {
        CarDto bookedCar = carService.bookCar(id);
        return ResponseEntity.ok(bookedCar);
    }

    /**
     * Returns the car with the specified ID.
     *
     * @param id the ID of the car to return
     * @return a {@link ResponseEntity} containing the returned {@link CarDto}
     */
    @PostMapping("/return/{id}")
    public ResponseEntity<CarDto> returnCar(@PathVariable Long id) {
        CarDto returnedCar = carService.returnCar(id);
        return ResponseEntity.ok(returnedCar);
    }

    /**
     * Retrieves cars available between two dates, optionally converting prices to the specified currency.
     *
     * @param from     the start date of the availability range (ISO date format)
     * @param to       the end date of the availability range (ISO date format)
     * @param currency the currency code for price conversion (default is "USD")
     * @return a list of {@link AvailableCarDto} for cars available in the given range
     */
    @GetMapping("/available")
    public List<AvailableCarDto> getAvailableBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "USD") String currency) {
        return carService.getAvailableBetween(from, to, currency);
    }

    /**
     * Retrieves the car with the specified ID.
     *
     * @param id the ID of the car to retrieve
     * @return the {@link CarDto} corresponding to the given ID
     */
    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

}
