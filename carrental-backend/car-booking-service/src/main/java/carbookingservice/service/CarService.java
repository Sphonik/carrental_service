// src/main/java/com/carrental/service/CarService.java
package carbookingservice.service;

import carbookingservice.dto.AvailableCarDto;
import carbookingservice.dto.CarDto;
import carbookingservice.exception.CarNotAvailableException;
import carbookingservice.exception.EntityNotFoundException;
import carbookingservice.exception.InvalidBookingRequestException;
import carbookingservice.integration.CurrencyConverterClient;
import carbookingservice.mapper.CarMapper;
import carbookingservice.model.Car;
import carbookingservice.repository.BookingRepository;
import carbookingservice.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    private final CarRepository     carRepository;
    private final BookingRepository bookingRepository;
    private final CarMapper         carMapper;
    private final CurrencyConverterClient currencyClient;

    public CarService(CarRepository carRepository,
                      BookingRepository bookingRepository,
                      CarMapper carMapper,
                      CurrencyConverterClient currencyClient) {
        this.carRepository      = carRepository;
        this.bookingRepository  = bookingRepository;
        this.carMapper          = carMapper;
        this.currencyClient     = currencyClient;
    }

    /**
     * Liefert alle aktuell verfügbaren Fahrzeuge, die im Zeitraum [from,to] nicht gebucht sind,
     * inklusive Preisumrechnung in die gewünschte Währung.
     */
    public List<AvailableCarDto> getAvailableBetween(LocalDate from,
                                                     LocalDate to,
                                                     String     currency) {

        if (from.isAfter(to)) {
            throw new InvalidBookingRequestException("from must be on or before to");
        }

        return carRepository.findByAvailableTrue().stream()
                .filter(car -> !bookingRepository
                        .existsByCarRentedIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                car.getId(), to, from))
                .map(car -> mapToDto(car, currency))
                .toList();
    }

    /** Alle aktuell verfügbaren Autos (ohne Datumsfilter). */
    public List<CarDto> getAvailableCarDtos() {
        return carMapper.toDtoList(carRepository.findByAvailableTrue());
    }

    /** Ein Auto buchen → Availability auf false setzen. */
    public CarDto bookCar(String id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));

        if (!car.isAvailable()) {
            throw new CarNotAvailableException(id);
        }
        car.setAvailable(false);
        return carMapper.toDto(carRepository.save(car));
    }

    /** Auto zurückgeben → Availability auf true setzen. */
    public CarDto returnCar(String id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));

        if (car.isAvailable()) {
            throw new CarNotAvailableException(id);
        }
        car.setAvailable(true);
        return carMapper.toDto(carRepository.save(car));
    }

    /** Einzelnes Auto abrufen. */
    public CarDto getCarById(String id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car", id));
        return carMapper.toDto(car);
    }

    /* ------------------------------------------------ private helpers ------------------------------------------- */

    private AvailableCarDto mapToDto(Car car, String currency) {
        BigDecimal converted = currencyClient.convert(car.getPricePerDay(), currency);
        return new AvailableCarDto(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getPricePerDay(),
                converted.setScale(2, RoundingMode.HALF_UP),
                currency.toUpperCase(),
                car.getYear(),
                car.getColor(),
                car.getFuelType().name(),
                car.isAutomatic(),
                car.getPickupLocation()
        );
    }
}
