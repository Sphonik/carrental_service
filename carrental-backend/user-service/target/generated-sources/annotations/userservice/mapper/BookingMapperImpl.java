package userservice.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-27T13:36:24+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDto toDto(Booking booking) {
        if ( booking == null ) {
            return null;
        }

        String userId = null;
        String carId = null;
        String id = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        BigDecimal totalCost = null;
        String currency = null;

        userId = booking.getBookedById();
        carId = booking.getCarRentedId();
        id = booking.getId();
        startDate = booking.getStartDate();
        endDate = booking.getEndDate();
        totalCost = booking.getTotalCost();
        currency = booking.getCurrency();

        BookingDto bookingDto = new BookingDto( id, userId, carId, startDate, endDate, totalCost, currency );

        return bookingDto;
    }

    @Override
    public List<BookingDto> toDtoList(List<Booking> bookings) {
        if ( bookings == null ) {
            return null;
        }

        List<BookingDto> list = new ArrayList<BookingDto>( bookings.size() );
        for ( Booking booking : bookings ) {
            list.add( toDto( booking ) );
        }

        return list;
    }
}
