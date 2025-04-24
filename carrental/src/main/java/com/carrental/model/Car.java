package com.carrental.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.swing.*;
import java.math.BigDecimal;

@Document("cars")
public class Car {

    @Id
    private String id;

    private String make;
    private String model;
    private Integer year;
    private String color;
    private FuelType fuelType;
    private boolean automatic;
    private BigDecimal pricePerDay;   // USD
    private String pickupLocation;
    private boolean available;

    public Car() {}

    public Car(String make,
               String model,
               Integer year,
               String color,
               FuelType fuelType,
               boolean automatic,
               BigDecimal pricePerDay,
               String pickupLocation) {
        this.make           = make;
        this.model          = model;
        this.year           = year;
        this.color          = color;
        this.fuelType       = fuelType;
        this.automatic      = automatic;
        this.pricePerDay    = pricePerDay;
        this.pickupLocation = pickupLocation;
        this.available      = true;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    public boolean isAutomatic() { return automatic; }
    public void setAutomatic(boolean automatic) { this.automatic = automatic; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean rentedStatus) { this.available = rentedStatus; }

}
