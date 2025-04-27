package com.carrental.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object representing a booking.
 *
 * @param id         unique identifier of the booking
 * @param userId     identifier of the user who made the booking
 * @param carId      identifier of the booked car
 * @param startDate  start date of the booking period
 * @param endDate    end date of the booking period
 * @param totalCost  total cost of the booking in the specified currency
 * @param currency   currency code for the total cost (e.g., "USD", "EUR")
 */
public record BookingDto(
        Integer id,
        Integer userId,
        Integer carId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalCost,
        String currency
) {}
