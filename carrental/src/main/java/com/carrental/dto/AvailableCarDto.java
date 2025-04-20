package com.carrental.dto;

import java.math.BigDecimal;

public record AvailableCarDto(
        Integer id,
        String make,
        String model,
        BigDecimal pricePerDayUsd,
        BigDecimal pricePerDay,
        String currency
) {}
