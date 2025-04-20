package com.carrental.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* ------------  JPA‑Beziehungen  ------------ */
    @ManyToOne(fetch = FetchType.LAZY)          // User ↔ Booking   N:1
    @JoinColumn(name = "booked_by", nullable = false)
    private User bookedBy;

    @ManyToOne(fetch = FetchType.LAZY)          // Car  ↔ Booking   N:1
    @JoinColumn(name = "car_rented", nullable = false)
    private Car carRented;
    /* ------------------------------------------- */

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Column(nullable = false, length = 3)
    private String currency;

    /* --------------  ctor / getter / setter -------------- */

    public Booking() {}

    public Booking(User bookedBy, Car carRented,
                   LocalDate startDate, LocalDate endDate,
                   BigDecimal pricePerDayUsd, String currency) {

        this.bookedBy   = bookedBy;
        this.carRented  = carRented;
        this.startDate  = startDate;
        this.endDate    = endDate;
        this.currency   = currency;
        this.totalCost  = calcTotalCost(pricePerDayUsd);
    }

    private BigDecimal calcTotalCost(BigDecimal pricePerDayUsd) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return pricePerDayUsd.multiply(BigDecimal.valueOf(days));
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }

    public void setCarRented(Car carRented) {
        this.carRented = carRented;
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

    public User getBookedBy() {
        return bookedBy;
    }

    public Integer getId() {
        return id;
    }

    public Car getCarRented() {
        return carRented;
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





