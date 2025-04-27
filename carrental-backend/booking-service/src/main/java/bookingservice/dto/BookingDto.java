package bookingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingDto(
        String id,
        String userId,
        String carId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalCost,
        String currency
) {}
