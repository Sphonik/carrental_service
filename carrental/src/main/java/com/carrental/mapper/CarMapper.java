package com.carrental.mapper;

import com.carrental.dto.CarDto;
import com.carrental.model.Car;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting between {@link Car} entities and {@link CarDto} objects.
 * <p>
 * Uses MapStruct to generate implementations for mapping:
 * <ul>
 *   <li>{@link Car} → {@link CarDto}</li>
 *   <li>{@link CarDto} → {@link Car}</li>
 *   <li>Lists of {@link Car} ↔ Lists of {@link CarDto}</li>
 * </ul>
 */
@Mapper(componentModel = "spring")
public interface CarMapper {

    /**
     * Converts a {@link Car} entity to its corresponding {@link CarDto}.
     *
     * @param car the Car entity to map
     * @return the mapped CarDto
     */
    CarDto toDto(Car car);

    /**
     * Converts a {@link CarDto} to its corresponding {@link Car} entity.
     *
     * @param dto the CarDto to map
     * @return the mapped Car entity
     */
    Car toEntity(CarDto dto);

    /**
     * Converts a list of {@link Car} entities to a list of {@link CarDto} objects.
     *
     * @param cars the list of Car entities to map
     * @return the list of mapped CarDto objects
     */
    List<CarDto> toDtoList(List<Car> cars);
}
