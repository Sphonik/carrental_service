package com.carrental.controller;
import com.carrental.dto.AvailableCarDto;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import com.carrental.dto.CarDto;
import com.carrental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<CarDto> bookCar(@PathVariable Long id) {
        CarDto bookedCar = carService.bookCar(id);
        return ResponseEntity.ok(bookedCar);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<CarDto> returnCar(@PathVariable Long id) {
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


}
