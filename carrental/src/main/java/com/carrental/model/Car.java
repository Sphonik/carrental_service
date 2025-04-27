package com.carrental.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entity representing a car in the rental system.
 * <p>
 * Maps to the "cars" table and stores details such as make, model,
 * rental price, and availability status.
 */
@Entity
@Table(name = "cars")
public class Car {

    /** Unique identifier of the car. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Manufacturer of the car (e.g., Toyota, BMW). */
    @Column(nullable = false)
    private String make;

    /** Specific model of the car (e.g., Corolla, 3 Series). */
    @Column(nullable = false)
    private String model;

    /** Manufacturing year of the car. */
    @Column(nullable = false)
    private Integer year;

    /** Exterior color of the car. */
    @Column(nullable = false)
    private String color;

    /** Fuel type of the car (e.g., GASOLINE, ELECTRIC). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    /** Indicates whether the car has an automatic transmission. */
    @Column(nullable = false)
    private boolean automatic;

    /** Daily rental price stored in USD. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    /** Location where the car can be picked up. */
    @Column(nullable = false)
    private String pickupLocation;

    /**
     * Rental status of the car.
     * <ul>
     *   <li>{@code false} = available for booking</li>
     *   <li>{@code true} = currently rented</li>
     * </ul>
     */
    @Column(nullable = false)
    private boolean available;

    /**
     * Default constructor required by JPA.
     */
    public Car() {}

    /**
     * Constructs a new Car with the specified attributes.
     * <p>
     * Newly created cars are available for booking by default.
     *
     * @param make           manufacturer of the car
     * @param model          specific model of the car
     * @param year           manufacturing year
     * @param color          exterior color
     * @param fuelType       fuel type enum
     * @param automatic      {@code true} if automatic transmission, {@code false} otherwise
     * @param pricePerDay    daily rental price in USD
     * @param pickupLocation location where the car can be picked up
     */
    public Car(String make,
               String model,
               Integer year,
               String color,
               FuelType fuelType,
               boolean automatic,
               BigDecimal pricePerDay,
               String pickupLocation) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.fuelType = fuelType;
        this.automatic = automatic;
        this.pricePerDay = pricePerDay;
        this.pickupLocation = pickupLocation;
        this.available = false; // default: available for booking
    }

    /**
     * Returns the unique identifier of the car.
     *
     * @return car ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the car.
     *
     * @param id car ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the manufacturer of the car.
     *
     * @return make of the car
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the manufacturer of the car.
     *
     * @param make make to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Returns the model of the car.
     *
     * @return model of the car
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the car.
     *
     * @param model model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the manufacturing year of the car.
     *
     * @return year of manufacture
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the manufacturing year of the car.
     *
     * @param year year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Returns the exterior color of the car.
     *
     * @return color of the car
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the exterior color of the car.
     *
     * @param color color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Returns the fuel type of the car.
     *
     * @return fuel type enum
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Sets the fuel type of the car.
     *
     * @param fuelType fuel type to set
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Returns whether the car has an automatic transmission.
     *
     * @return {@code true} if automatic, {@code false} otherwise
     */
    public boolean isAutomatic() {
        return automatic;
    }

    /**
     * Sets the transmission type of the car.
     *
     * @param automatic {@code true} for automatic, {@code false} for manual
     */
    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    /**
     * Returns the daily rental price in USD.
     *
     * @return price per day
     */
    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    /**
     * Sets the daily rental price in USD.
     *
     * @param pricePerDay price to set
     */
    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    /**
     * Returns the pickup location of the car.
     *
     * @return pickup location
     */
    public String getPickupLocation() {
        return pickupLocation;
    }

    /**
     * Sets the pickup location of the car.
     *
     * @param pickupLocation location to set
     */
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * Returns the rental status of the car.
     *
     * @return {@code true} if rented, {@code false} if available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the rental status of the car.
     *
     * @param available {@code true} to mark as rented, {@code false} to mark as available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
