package carbookingservice.mapper;

import carbookingservice.dto.BookingDto;
import carbookingservice.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting Booking entities into BookingDto objects.
 * <p>
 * Uses MapStruct to generate the implementation at compile time,
 * handling field mappings and collection conversions.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {

    /**
     * Converts a single Booking entity to its DTO representation.
     * <p>
     * The {@code bookedById} and {@code carRentedId} fields from the entity
     * are mapped to {@code userId} and {@code carId} in the DTO, respectively.
     *
     * @param booking the Booking entity to convert
     * @return a BookingDto containing the booking details
     */
    @Mapping(source = "bookedById", target = "userId")
    @Mapping(source = "carRentedId", target = "carId")
    BookingDto toDto(Booking booking);

    /**
     * Converts a list of Booking entities into a list of BookingDto objects.
     *
     * @param bookings the list of Booking entities to convert
     * @return a list of BookingDto instances
     */
    List<BookingDto> toDtoList(List<Booking> bookings);
}
