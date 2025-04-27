package com.carrental.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating a new booking.
 * <p>
 * Contains the user ID, car ID, booking period, and optional currency code.
 *
 * @param userId    ID of the user making the booking
 * @param carId     ID of the car to be booked
 * @param startDate start date of the rental period
 * @param endDate   end date of the rental period
 * @param currency  optional ISO currency code (e.g., "EUR"); defaults to "USD" if not provided
 */
public record BookingRequestDto(
        Integer userId,
        Long carId,
        LocalDate startDate,
        LocalDate endDate,
        String currency
) {}
