package com.carrental.repository;

import com.carrental.dto.AvailableCarDto;
import com.carrental.dto.CarDto;
import com.carrental.exception.EntityNotFoundException;
import com.carrental.integration.CurrencyConverterClient;
import com.carrental.mapper.CarMapper;
import com.carrental.model.Car;
import com.carrental.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarRepositoryTest {

    @Mock
    private CarRepository carRepository;  // Mock the CarRepository

    @Mock
    private CarMapper carMapper;  // Mock the CarMapper

    @Mock
    private CurrencyConverterClient currencyConverterClient;  // Mock the CurrencyConverterClient

    @InjectMocks
    private CarService carService;  // Inject mocks into CarService

    private Car car1;
    private Car car2;

    @BeforeEach
    void setUp() {
        // Initialize mock car data
        car1 = new Car();
        car1.setId(1);
        car1.setModel("Toyota Corolla");
        car1.setAvailable(true);
        car1.setPricePerDay(BigDecimal.valueOf(50));

        car2 = new Car();
        car2.setId(2);
        car2.setModel("Honda Civic");
        car2.setAvailable(true);
        car2.setPricePerDay(BigDecimal.valueOf(60));
    }

 /**   @Test
    void testGetAvailableBetween() {
        // Arrange: Mock repository method
        LocalDate from = LocalDate.of(2025, 5, 1);
        LocalDate to = LocalDate.of(2025, 5, 10);

        when(carRepository.findAvailableBetween(from, to)).thenReturn(List.of(car1, car2));
        when(currencyConverterClient.convert(any(), eq("USD"))).thenReturn(BigDecimal.valueOf(50));

        // Stubbing the CarMapper
        when(carMapper.toAvailableDto(car1, BigDecimal.valueOf(50), "USD")).thenReturn(
                new AvailableCarDto(car1.getId(), car1.getMake(), car1.getModel(), car1.getPricePerDay(),
                        BigDecimal.valueOf(50), "USD", car1.getYear(), car1.getColor(),
                        car1.getFuelType().toString(), car1.isAutomatic(), car1.getPickupLocation())
        );
        when(carMapper.toAvailableDto(car2, BigDecimal.valueOf(60), "USD")).thenReturn(
                new AvailableCarDto(car2.getId(), car2.getMake(), car2.getModel(), car2.getPricePerDay(),
                        BigDecimal.valueOf(60), "USD", car2.getYear(), car2.getColor(),
                        car2.getFuelType().toString(), car2.isAutomatic(), car2.getPickupLocation())
        );

        // Act: Call the service method
        List<AvailableCarDto> availableCars = carService.getAvailableBetween(from, to, "USD");

        // Assert: Verify the results
        assertNotNull(availableCars, "Available cars should not be null");
        assertEquals(2, availableCars.size(), "There should be 2 available cars");
        assertTrue(availableCars.stream().anyMatch(dto -> dto.getModel().equals("Toyota Corolla")),
                "The available cars should contain Toyota Corolla");
        assertTrue(availableCars.stream().anyMatch(dto -> dto.getModel().equals("Honda Civic")),
                "The available cars should contain Honda Civic");
    }

    @Test
    void testGetAvailableCarDtos() {
        // Arrange: Mock repository method
        when(carRepository.findByAvailableTrue()).thenReturn(List.of(car1, car2));

        // Stubbing the CarMapper
        when(carMapper.toDtoList(List.of(car1, car2))).thenReturn(
                List.of(new CarDto(car1.getId(), car1.getMake(), car1.getModel(), car1.getYear(),
                                car1.getColor(), car1.getFuelType().toString(), car1.isAutomatic(),
                                car1.getPricePerDay(), car1.getPickupLocation(), car1.isAvailable()),
                        new CarDto(car2.getId(), car2.getMake(), car2.getModel(), car2.getYear(),
                                car2.getColor(), car2.getFuelType().toString(), car2.isAutomatic(),
                                car2.getPricePerDay(), car2.getPickupLocation(), car2.isAvailable()))
        );

        // Act: Call the service method
        List<CarDto> availableCarDtos = carService.getAvailableCarDtos();

        // Assert: Verify the results
        assertNotNull(availableCarDtos, "Car DTOs should not be null");
        assertEquals(2, availableCarDtos.size(), "There should be 2 car DTOs");
        assertTrue(availableCarDtos.stream().anyMatch(dto -> dto.getModel().equals("Toyota Corolla")),
                "The list should contain Toyota Corolla");
        assertTrue(availableCarDtos.stream().anyMatch(dto -> dto.getModel().equals("Honda Civic")),
                "The list should contain Honda Civic");
    }

    @Test
    void testBookCar() {
        // Arrange: Mock repository method to return car when searching by ID
        when(carRepository.findById(1L)).thenReturn(Optional.of(car1));

        // Act: Call the service method
        CarDto bookedCarDto = carService.bookCar(1L);

        // Assert: Verify that the car is booked and not available
        assertNotNull(bookedCarDto, "Car DTO should not be null");
        assertFalse(car1.isAvailable(), "The car should be marked as not available");
    }
**/
    @Test
    void testCarNotFoundOnBooking() {
        // Arrange: Mock repository method to return empty when car not found
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert: Verify that EntityNotFoundException is thrown when car not found
        assertThrows(EntityNotFoundException.class, () -> carService.bookCar(99L),
                "Car with ID 99 should not be found");
    }
}
