package com.carrental.repository;

import com.carrental.model.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    @Test
    void stubFindByBookedById() {
        Booking fake = new Booking();
        fake.setStartDate(LocalDate.now());

        when(bookingRepository.findByBookedBy_Id(1))
                .thenReturn(List.of(fake));

        List<Booking> bookings = bookingRepository.findByBookedBy_Id(1);

        assertEquals(1, bookings.size());
        assertSame(fake, bookings.get(0));
        verify(bookingRepository).findByBookedBy_Id(1);
    }
}
