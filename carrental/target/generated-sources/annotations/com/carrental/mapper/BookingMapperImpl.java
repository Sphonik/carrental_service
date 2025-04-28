package com.carrental.mapper;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.model.Booking;
import com.carrental.model.Car;
import com.carrental.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-28T13:53:41+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDto toDto(Booking entity) {
        if ( entity == null ) {
            return null;
        }

        Integer userId = null;
        Integer carId = null;
        Integer id = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        BigDecimal totalCost = null;
        String currency = null;

        userId = entityBookedById( entity );
        carId = entityCarRentedId( entity );
        id = entity.getId();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        totalCost = entity.getTotalCost();
        currency = entity.getCurrency();

        BookingDto bookingDto = new BookingDto( id, userId, carId, startDate, endDate, totalCost, currency );

        return bookingDto;
    }

    @Override
    public List<BookingDto> toDtoList(List<Booking> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BookingDto> list = new ArrayList<BookingDto>( entities.size() );
        for ( Booking booking : entities ) {
            list.add( toDto( booking ) );
        }

        return list;
    }

    @Override
    public Booking toEntity(BookingRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setStartDate( dto.startDate() );
        booking.setEndDate( dto.endDate() );
        booking.setCurrency( dto.currency() );

        return booking;
    }

    private Integer entityBookedById(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        User bookedBy = booking.getBookedBy();
        if ( bookedBy == null ) {
            return null;
        }
        Integer id = bookedBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Integer entityCarRentedId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Car carRented = booking.getCarRented();
        if ( carRented == null ) {
            return null;
        }
        Integer id = carRented.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
