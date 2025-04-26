package bookingservice.dto;

import java.time.LocalDate;

/**
 * Payload für einen neuen Buchungs‑POST.
 * - currency: ISO‑Code, z. B. "EUR" (USD wird intern abgefangen)
 */
public record BookingRequestDto(
        String userId,
        String carId,
        LocalDate startDate,
        LocalDate endDate,
        String currency
) {}


