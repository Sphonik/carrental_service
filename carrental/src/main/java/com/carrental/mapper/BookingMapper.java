package com.carrental.mapper;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.model.Booking;
import com.carrental.model.Car;
import com.carrental.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "bookedBy.id", target = "userId")
    @Mapping(source = "carRented.id", target = "carId")
    BookingDto toDto(Booking entity);

    List<BookingDto> toDtoList(List<Booking> entities);

    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "bookedBy", ignore = true)
    @Mapping(target = "carRented", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "id", ignore = true) // ← diese Zeile behebt den letzten Fehler
    Booking toEntity(BookingRequestDto dto);
}
