package carbookingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a car booking stored in MongoDB.
 * <p>
 * Contains identifiers for the user and car, rental period, total cost, and currency.
 * Indexed by car and date range to optimize availability queries.
 */
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

    /**
     * Default constructor required by persistence frameworks.
     */
    public Booking() {}

    /**
     * Creates a new booking with the given details and calculates the total cost.
     *
     * @param bookedById      identifier of the user who made the booking
     * @param carRentedId     identifier of the car being booked
     * @param startDate       start date of the rental period
     * @param endDate         end date of the rental period
     * @param pricePerDayUsd  daily rental price in USD used for cost calculation
     * @param currency        ISO currency code for the booking
     */
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

    /**
     * Calculates the total cost for the booking period.
     *
     * @param pricePerDayUsd daily rental price in USD
     * @return total cost for the entire rental period
     */
    private BigDecimal calcTotalCost(BigDecimal pricePerDayUsd) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return pricePerDayUsd.multiply(BigDecimal.valueOf(days));
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookedById() {
        return bookedById;
    }

    public void setBookedById(String bookedById) {
        this.bookedById = bookedById;
    }

    public String getCarRentedId() {
        return carRentedId;
    }

    public void setCarRentedId(String carRentedId) {
        this.carRentedId = carRentedId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
