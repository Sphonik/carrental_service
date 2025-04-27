package carbookingservice.dto;

import java.math.BigDecimal;

public record AvailableCarDto(
        String id,
        String make,
        String model,
        BigDecimal pricePerDayUsd,
        BigDecimal pricePerDay,
        String currency,
        Integer year,
        String color,
        String fuelType,
        boolean automatic,
        String pickupLocation
) {}
