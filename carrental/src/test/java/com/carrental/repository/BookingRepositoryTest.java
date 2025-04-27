package com.carrental.repository;

import com.carrental.model.Booking;
import com.carrental.model.User;
import com.carrental.model.Car;
import com.carrental.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        // Create and save test bookings
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        // Create and save test cars
        Car car1 = new Car();
        car1.setId(1);
        car1.setModel("Toyota Corolla");

        Car car2 = new Car();
        car2.setId(2);
        car2.setModel("Honda Civic");
        booking1 = new Booking();
        booking1.setBookedBy(user);
        booking1.setCarRented(new Car());  // Assuming Car is properly set
        booking1.setStartDate(LocalDate.of(2025, 5, 1));
        booking1.setEndDate(LocalDate.of(2025, 5, 10));
        booking1 = bookingRepository.save(booking1);

        booking2 = new Booking();
        booking2.setBookedBy(user);
        booking2.setCarRented(new Car());  // Assuming Car is properly set
        booking2.setStartDate(LocalDate.of(2025, 5, 11));
        booking2.setEndDate(LocalDate.of(2025, 5, 20));
        booking2 = bookingRepository.save(booking2);
    }

    @Test
    void testFindByBookedById() {
        List<Booking> bookings = bookingRepository.findByBookedBy_Id(1);
        assertEquals(2, bookings.size(), "Should return 2 bookings for user with ID 1");
    }

    @Test
    void testExistsOverlapping() {
        boolean overlapping = bookingRepository.existsOverlapping(
                booking1.getCarRented().getId(),
                LocalDate.of(2025, 5, 5),
                LocalDate.of(2025, 5, 15)
        );
        assertTrue(overlapping, "There should be an overlapping booking");
    }
}
