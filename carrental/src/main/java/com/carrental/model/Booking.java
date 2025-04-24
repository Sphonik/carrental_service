package com.carrental.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Document("bookings")
@CompoundIndex(name = "car_date_idx",
        def  = "{'carRentedId':1,'startDate':1,'endDate':1}")
public class Booking {

    @Id
    private String id;

    private String bookedById;
    private String carRentedId;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal totalCost;
    private String currency;

    public Booking() {}

    public Booking(String bookedById,
                   String carRentedId,
                   LocalDate startDate,
                   LocalDate endDate,
                   BigDecimal pricePerDayUsd,
                   String currency) {
        this.bookedById  = bookedById;
        this.carRentedId = carRentedId;
        this.startDate   = startDate;
        this.endDate     = endDate;
        this.currency    = currency;
        this.totalCost   = calcTotalCost(pricePerDayUsd);
    }

    private BigDecimal calcTotalCost(BigDecimal pricePerDayUsd) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return pricePerDayUsd.multiply(BigDecimal.valueOf(days));
    }

    public void setCarRentedId(String carRentedId) {
        this.carRentedId = carRentedId;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setBookedById(String bookedById) {
        this.bookedById = bookedById;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getId() {
        return id;
    }
    public String getBookedById() {
        return bookedById;
    }
    public String getCarRentedId() {
        return carRentedId;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    public String getCurrency() {
        return currency;
    }
}





