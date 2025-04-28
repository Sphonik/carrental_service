package carbookingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Represents a car available for rental, stored in MongoDB.
 * <p>
 * Contains details such as make, model, year, appearance, fuel type,
 * transmission, pricing, pickup location, and availability status.
 */
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
    private BigDecimal pricePerDay;
    private String pickupLocation;
    private boolean available;

    /**
     * Default constructor required by persistence frameworks.
     */
    public Car() {}

    /**
     * Constructs a new Car with the specified attributes.
     * <p>
     * The car is marked as available upon creation.
     *
     * @param make           manufacturer of the car
     * @param model          model name of the car
     * @param year           manufacture year
     * @param color          exterior color
     * @param fuelType       type of fuel (e.g., ELECTRIC, DIESEL, PETROL)
     * @param automatic      {@code true} for automatic transmission
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
        this.available = true;
    }

    /**
     * @return unique identifier of the car
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the car.
     *
     * @param id the car ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return manufacturer name
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the manufacturer name.
     *
     * @param make the manufacturer to set
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * @return model name
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model name.
     *
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return manufacture year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the manufacture year.
     *
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return exterior color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the exterior color.
     *
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return type of fuel used by the car
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Sets the fuel type.
     *
     * @param fuelType the fuel type to set
     */
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * @return {@code true} if the car has automatic transmission
     */
    public boolean isAutomatic() {
        return automatic;
    }

    /**
     * Sets the transmission type.
     *
     * @param automatic {@code true} for automatic transmission
     */
    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    /**
     * @return daily rental price in USD
     */
    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    /**
     * Sets the daily rental price in USD.
     *
     * @param pricePerDay the price per day to set
     */
    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    /**
     * @return pickup location for the car
     */
    public String getPickupLocation() {
        return pickupLocation;
    }

    /**
     * Sets the pickup location.
     *
     * @param pickupLocation the location to set
     */
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * @return {@code true} if the car is currently available for booking
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the availability status of the car.
     *
     * @param available {@code true} if the car should be marked available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
