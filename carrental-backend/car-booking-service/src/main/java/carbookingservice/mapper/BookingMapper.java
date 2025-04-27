package carbookingservice.mapper;

import carbookingservice.dto.BookingDto;
import carbookingservice.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    /**
     * Map Booking → BookingDto, pulling the two DBRef IDs out of the nested objects.
     */
    @Mapping(source = "bookedById", target = "userId")
    @Mapping(source = "carRentedId", target = "carId")
    BookingDto toDto(Booking booking);

    List<BookingDto> toDtoList(List<Booking> bookings);
}
