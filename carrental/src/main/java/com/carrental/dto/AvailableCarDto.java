package com.carrental.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing a car available for booking,
 * including pricing information and attributes.
 *
 * @param id              unique identifier of the car
 * @param make            manufacturer of the car (e.g., Toyota, BMW)
 * @param model           specific model of the car (e.g., Corolla, 3 Series)
 * @param pricePerDayUsd  base rental price per day in USD
 * @param pricePerDay     rental price per day converted to the requested currency
 * @param currency        currency code for the converted price (e.g., "EUR", "USD")
 * @param year            manufacturing year of the car
 * @param color           exterior color of the car
 * @param fuelType        type of fuel the car uses (e.g., "Gasoline", "Electric")
 * @param automatic       whether the car has an automatic transmission
 * @param pickupLocation  location where the car can be picked up
 */
public record AvailableCarDto(
        Integer id,
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
