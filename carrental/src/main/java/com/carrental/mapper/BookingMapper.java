package com.carrental.mapper;

import com.carrental.dto.BookingDto;
import com.carrental.dto.BookingRequestDto;
import com.carrental.model.Booking;
import com.carrental.model.Car;
import com.carrental.model.User;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between {@link Booking} entities and DTOs.
 * <p>
 * Uses MapStruct to generate implementations for mapping:
 * <ul>
 *   <li>{@link Booking} → {@link BookingDto}</li>
 *   <li>{@link BookingRequestDto} → {@link Booking}</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {

    /**
     * Converts a {@link Booking} entity to a {@link BookingDto}.
     * <p>
     * Maps the {@code bookedBy.id} property to {@code userId} and
     * {@code carRented.id} to {@code carId}.
     *
     * @param entity the Booking entity to convert
     * @return the corresponding BookingDto
     */
    @Mapping(source = "bookedBy.id", target = "userId")
    @Mapping(source = "carRented.id", target = "carId")
    BookingDto toDto(Booking entity);

    /**
     * Converts a list of {@link Booking} entities to a list of {@link BookingDto}.
     *
     * @param entities the list of Booking entities to convert
     * @return the list of corresponding BookingDto objects
     */
    List<BookingDto> toDtoList(List<Booking> entities);

    /**
     * Converts a {@link BookingRequestDto} to a {@link Booking} entity.
     * <p>
     * Inverse of {@link #toDto(Booking)}, but ignores the following fields:
     * <ul>
     *   <li>{@code bookedBy}</li>
     *   <li>{@code carRented}</li>
     *   <li>{@code totalCost}</li>
     *   <li>{@code id}</li>
     * </ul>
     *
     * @param dto the BookingRequestDto to convert
     * @return the new Booking entity (without associations or generated values)
     */
    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "bookedBy", ignore = true)
    @Mapping(target = "carRented", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "id", ignore = true)
    Booking toEntity(BookingRequestDto dto);
}
