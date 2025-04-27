package carbookingservice.controller;

import carbookingservice.dto.AvailableCarDto;
import carbookingservice.dto.CarDto;
import carbookingservice.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDto> getAvailableCars() {
        return carService.getAvailableCarDtos();   // DTO‑Liste
    }

    @PostMapping("/book/{id}")
    public ResponseEntity<CarDto> bookCar(@PathVariable String id) {
        CarDto bookedCar = carService.bookCar(id);
        return ResponseEntity.ok(bookedCar);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<CarDto> returnCar(@PathVariable String id) {
        CarDto returnedCar = carService.returnCar(id);
        return ResponseEntity.ok(returnedCar);
    }

    @GetMapping("/available")
    public List<AvailableCarDto> getAvailableBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "USD") String currency) {

        return carService.getAvailableBetween(from, to, currency);
    }

    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable String id) {
        return carService.getCarById(id);
    }


}
