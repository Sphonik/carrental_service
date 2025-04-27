package carbookingservice.dto;

import java.math.BigDecimal;

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

