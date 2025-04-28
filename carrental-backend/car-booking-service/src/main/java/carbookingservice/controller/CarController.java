package carbookingservice.controller;

import carbookingservice.dto.AvailableCarDto;
import carbookingservice.dto.CarDto;
import carbookingservice.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing car operations.
 * <p>
 * Provides endpoints to list available cars, book and return cars,
 * retrieve car details, and query availability within a date range.
 */
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    /**
     * Constructs a CarController with the given CarService.
     *
     * @param carService service layer handling car-related operations
     */
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Retrieves all cars that are currently available for booking.
     *
     * @return a list of CarDto objects representing available cars
     */
    @GetMapping
    public List<CarDto> getAvailableCars() {
        return carService.getAvailableCarDtos();
    }

    /**
     * Books the car identified by the given ID.
     *
     * @param id the identifier of the car to book
     * @return a ResponseEntity containing the CarDto of the booked car
     */
    @PostMapping("/book/{id}")
    public ResponseEntity<CarDto> bookCar(@PathVariable String id) {
        CarDto bookedCar = carService.bookCar(id);
        return ResponseEntity.ok(bookedCar);
    }

    /**
     * Returns the car identified by the given ID.
     *
     * @param id the identifier of the car to return
     * @return a ResponseEntity containing the CarDto of the returned car
     */
    @PostMapping("/return/{id}")
    public ResponseEntity<CarDto> returnCar(@PathVariable String id) {
        CarDto returnedCar = carService.returnCar(id);
        return ResponseEntity.ok(returnedCar);
    }

    /**
     * Retrieves cars available between the specified start and end dates,
     * with pricing converted to the given currency if necessary.
     *
     * @param from     the start date (inclusive) in ISO date format
     * @param to       the end date (inclusive) in ISO date format
     * @param currency the currency code for price calculations (default "USD")
     * @return a list of AvailableCarDto detailing available cars and pricing
     */
    @GetMapping("/available")
    public List<AvailableCarDto> getAvailableBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "USD") String currency) {

        return carService.getAvailableBetween(from, to, currency);
    }

    /**
     * Retrieves the details of a car by its ID.
     *
     * @param id the identifier of the car to retrieve
     * @return the CarDto representing the requested car
     */
    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable String id) {
        return carService.getCarById(id);
    }
}
