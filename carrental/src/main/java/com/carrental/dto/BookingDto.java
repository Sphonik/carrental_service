package com.carrental.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingDto(
        Integer id,
        Integer userId,
        Integer carId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalCost,
        String currency
) {}
