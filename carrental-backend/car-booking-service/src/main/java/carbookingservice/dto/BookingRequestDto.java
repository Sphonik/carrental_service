package carbookingservice.dto;

import java.time.LocalDate;

/**
 * Data transfer object for creating a new booking.
 * <p>
 * Contains the identifiers of the user and car, the rental period,
 * and the desired currency code for price calculation.
 *
 * @param userId    identifier of the user making the booking
 * @param carId     identifier of the car to be booked
 * @param startDate start date of the rental period
 * @param endDate   end date of the rental period
 * @param currency  ISO currency code for price calculation (e.g., "EUR"; USD is handled internally)
 */
public record BookingRequestDto(
        String userId,
        String carId,
        LocalDate startDate,
        LocalDate endDate,
        String currency
) {}
