package carbookingservice.dto;

import java.math.BigDecimal;

/**
 * Data transfer object representing detailed information about a car,
 * including its specifications, pricing, location, and availability.
 *
 * @param id             unique identifier of the car
 * @param make           manufacturer name of the car
 * @param model          model name of the car
 * @param year           manufacture year of the car
 * @param color          exterior color of the car
 * @param fuelType       type of fuel used by the car (e.g., ELECTRIC, DIESEL, PETROL)
 * @param automatic      {@code true} if the car has an automatic transmission
 * @param pricePerDay    daily rental price in the configured currency
 * @param pickupLocation location where the car can be picked up
 * @param available      {@code true} if the car is currently available for booking
 */
public record CarDto(
        String id,
        String make,
        String model,
        Integer year,
        String color,
        String fuelType,
        boolean automatic,
        BigDecimal pricePerDay,
        String pickupLocation,
        boolean available
) {}
