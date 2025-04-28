package carbookingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data transfer object representing a booking made by a user,
 * including the booked car, rental period, and total cost.
 *
 * @param id         unique identifier of the booking
 * @param userId     identifier of the user who made the booking
 * @param carId      identifier of the car being booked
 * @param startDate  start date of the rental period
 * @param endDate    end date of the rental period
 * @param totalCost  total rental cost for the period
 * @param currency   currency code used for the total cost
 */
public record BookingDto(
        String id,
        String userId,
        String carId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalCost,
        String currency
) {}
