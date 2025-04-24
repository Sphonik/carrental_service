// src/main/java/com/carrental/config/DataSeeder.java
package com.carrental.config;

import com.carrental.model.*;
import com.carrental.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    private final CarRepository     carRepo;
    private final UserRepository    userRepo;
    private final BookingRepository bookingRepo;
    private final PasswordEncoder   encoder;   // NoOp

    public DataSeeder(CarRepository carRepo,
                      UserRepository userRepo,
                      BookingRepository bookingRepo,
                      PasswordEncoder encoder) {
        this.carRepo     = carRepo;
        this.userRepo    = userRepo;
        this.bookingRepo = bookingRepo;
        this.encoder     = encoder;
    }

    @Override
    public void run(String... args) {
        if (carRepo.count() == 0)     seedCars();
        if (userRepo.count() == 0)    seedUsers();
        if (bookingRepo.count() == 0) seedBookings();
    }

    /* ---------- Seeder-Hilfen ---------- */

    private void seedCars() {
        List<Car> cars = List.of(
                new Car("Tesla",   "Model 3", 2022, "white", FuelType.ELECTRIC, true,
                        new BigDecimal("69.90"), "Vienna Airport"),
                new Car("BMW",     "320d",    2021, "black", FuelType.DIESEL,   true,
                        new BigDecimal("59.90"), "Vienna City"),
                new Car("VW",      "Golf",    2019, "blue",  FuelType.PETROL,   false,
                        new BigDecimal("39.90"), "Salzburg Hbf"),
                new Car("Škoda",   "Octavia", 2020, "gray",  FuelType.PETROL,   true,
                        new BigDecimal("44.90"), "Graz Airport"),
                new Car("Renault", "Zoe",     2023, "green", FuelType.ELECTRIC, true,
                        new BigDecimal("49.90"), "Linz City")
        );
        carRepo.saveAll(cars);
        LOG.info("🚗  {} demo cars inserted", cars.size());
    }

    private void seedUsers() {
        List<User> users = List.of(
                new User("Charlie","Admin",  "admin", encoder.encode("admin"),  UserRole.ADMIN),
                new User("Alice",  "Wonder", "alice", encoder.encode("secret"), UserRole.USER),
                new User("Bob",    "Builder","bob",   encoder.encode("secret"), UserRole.TESTER)
        );
        userRepo.saveAll(users);
        LOG.info("👤  {} demo users inserted (NoOp – Klartext)", users.size());
    }

    private void seedBookings() {
        User alice = userRepo.findByUsername("alice")
                .orElseThrow(() ->
                        new IllegalStateException("User seeding failed"));

        Car tesla  = carRepo.findByMakeAndModel("Tesla", "Model 3")
                .orElseThrow(() ->
                        new IllegalStateException("Car seeding failed"));

        Booking b = new Booking(
                alice.getId(),
                tesla.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                new BigDecimal("209.70"),
                "USD"
        );
        bookingRepo.save(b);
        LOG.info("📑  1 demo booking inserted");
    }
}
