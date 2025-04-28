package carbookingservice.mapper;

import carbookingservice.dto.CarDto;
import carbookingservice.model.Car;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting between Car entities and CarDto objects.
 * <p>
 * Uses MapStruct to generate implementations at compile time,
 * handling individual object and collection mappings.
 */
@Mapper(componentModel = "spring")
public interface CarMapper {

    /**
     * Converts a Car entity to its corresponding DTO representation.
     *
     * @param car the Car entity to convert
     * @return a CarDto containing the entity’s data
     */
    CarDto toDto(Car car);

    /**
     * Converts a CarDto back to a Car entity.
     *
     * @param dto the CarDto to convert
     * @return a Car entity populated with the DTO’s data
     */
    Car toEntity(CarDto dto);

    /**
     * Converts a list of Car entities into a list of CarDto objects.
     *
     * @param cars the list of Car entities to convert
     * @return a list of CarDto instances
     */
    List<CarDto> toDtoList(List<Car> cars);
}
