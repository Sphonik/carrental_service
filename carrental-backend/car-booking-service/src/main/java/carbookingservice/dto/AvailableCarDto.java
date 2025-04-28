package carbookingservice.dto;

import java.math.BigDecimal;

/**
 * Data transfer object representing a car available for booking,
 * including pricing in both USD and a target currency, as well as
 * vehicle details and pickup location.
 *
 * @param id               unique identifier of the car
 * @param make             manufacturer name
 * @param model            model name
 * @param pricePerDayUsd   base daily rental price in USD
 * @param pricePerDay      converted daily rental price in the specified currency
 * @param currency         currency code used for the converted price
 * @param year             manufacture year of the car
 * @param color            exterior color of the car
 * @param fuelType         type of fuel used (e.g., ELECTRIC, DIESEL, PETROL)
 * @param automatic        {@code true} if the car has automatic transmission
 * @param pickupLocation   location where the car can be picked up
 */
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
