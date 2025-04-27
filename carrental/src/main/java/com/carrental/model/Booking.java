package com.carrental.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entity representing a car booking.
 * <p>
 * Maps to the "bookings" table and stores information about
 * who booked which car, the rental period, total cost, and currency.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    /** Unique identifier for this booking. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** The user who made the booking. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booked_by", nullable = false)
    private User bookedBy;

    /** The car that was booked. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_rented", nullable = false)
    private Car carRented;

    /** Start date of the booking period. */
    @Column(nullable = false)
    private LocalDate startDate;

    /** End date of the booking period. */
    @Column(nullable = false)
    private LocalDate endDate;

    /** Total cost of the booking in the specified currency. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalCost;

    /** ISO currency code for the booking cost (e.g., "USD", "EUR"). */
    @Column(nullable = false, length = 3)
    private String currency;

    /**
     * Default constructor required by JPA.
     */
    public Booking() {}

    /**
     * Constructs a new Booking.
     *
     * @param bookedBy      the {@link User} making the booking
     * @param carRented     the {@link Car} being booked
     * @param startDate     the start date of the rental period
     * @param endDate       the end date of the rental period
     * @param pricePerDayUsd the base price per day in USD
     * @param currency      the ISO currency code for total cost calculation
     */
    public Booking(User bookedBy, Car carRented,
                   LocalDate startDate, LocalDate endDate,
                   BigDecimal pricePerDayUsd, String currency) {
        this.bookedBy = bookedBy;
        this.carRented = carRented;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currency = currency;
        this.totalCost = calcTotalCost(pricePerDayUsd);
    }

    /**
     * Calculates the total cost based on the number of days between
     * {@code startDate} and {@code endDate} and the given daily USD price.
     *
     * @param pricePerDayUsd the base price per day in USD
     * @return total cost for the booking period
     */
    private BigDecimal calcTotalCost(BigDecimal pricePerDayUsd) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return pricePerDayUsd.multiply(BigDecimal.valueOf(days));
    }

    /** Sets the booking ID. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** Sets the user who booked. */
    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }

    /** Sets the car that was rented. */
    public void setCarRented(Car carRented) {
        this.carRented = carRented;
    }

    /** Sets the start date of the booking. */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /** Sets the end date of the booking. */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /** Sets the total cost of the booking. */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /** Sets the currency code for the booking cost. */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /** Returns the booking ID. */
    public Integer getId() {
        return id;
    }

    /** Returns the user who made the booking. */
    public User getBookedBy() {
        return bookedBy;
    }

    /** Returns the car that was booked. */
    public Car getCarRented() {
        return carRented;
    }

    /** Returns the start date of the booking. */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** Returns the end date of the booking. */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** Returns the total cost of the booking. */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /** Returns the ISO currency code for the booking cost. */
    public String getCurrency() {
        return currency;
    }
}
