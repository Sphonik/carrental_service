package com.carrental.service;
import com.carrental.dto.AvailableCarDto;
import com.carrental.exception.InvalidBookingRequestException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import com.carrental.dto.CarDto;
import com.carrental.exception.CarNotAvailableException;
import com.carrental.exception.EntityNotFoundException;
import com.carrental.integration.CurrencyConverterClient;
import com.carrental.mapper.CarMapper;
import com.carrental.model.Car;
import com.carrental.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CurrencyConverterClient currencyConverterClient;

    public CarService(CarRepository carRepository,
                      CarMapper carMapper,
                      CurrencyConverterClient currencyClient) {
        this.carRepository        = carRepository;
        this.carMapper            = carMapper;
        this.currencyConverterClient = currencyClient;
    }



    public List<AvailableCarDto> getAvailableBetween(LocalDate from, LocalDate to, String currency) {
        if (from.isAfter(to)) {
            throw new InvalidBookingRequestException("from must be on or before to");
        }

        List<Car> cars = carRepository.findAvailableBetween(from, to);
        return cars.stream()
                .map(car -> {
                    BigDecimal converted = currencyConverterClient.convert(car.getPricePerDay(), currency);
                    return toAvailableDto(car, converted, currency);
                })
                .toList();
    }

    /** List all cars that are currently available */
    public List<CarDto> getAvailableCarDtos() {
        return carMapper.toDtoList(carRepository.findByAvailableTrue());
    }

    /** Book a car or throw domain‑specific error */
    public CarDto bookCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));

        if (!car.isAvailable()) {
            throw new CarNotAvailableException(id);
        }
        car.setAvailable(false);
        return carMapper.toDto(carRepository.save(car));
    }

    /** Return a car or throw 404 / 409 if invalid */
    public CarDto returnCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));

        if (car.isAvailable()) {                     // already returned
            throw new CarNotAvailableException(id);
        }
        car.setAvailable(true);
        return carMapper.toDto(carRepository.save(car));
    }


    private AvailableCarDto toAvailableDto(Car car,
                                           BigDecimal priceConverted,
                                           String currency) {
        return new AvailableCarDto(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getPricePerDay(),
                priceConverted.setScale(2, RoundingMode.HALF_UP),
                currency.toUpperCase(),
                car.getYear(),
                car.getColor(),
                car.getFuelType().toString(),
                car.isAutomatic(),
                car.getPickupLocation()
        );
    }
    // Neue Methode: Auto nach ID abrufen
    public CarDto getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car with ID " + id + " does not exist"));

        // Mappt die Car-Entity auf ein CarDto
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


}


