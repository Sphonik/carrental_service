package userservice.mapper;

import userservice.dto.CarDto;
import userservice.model.Car;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarDto toDto(Car car);
    Car toEntity(CarDto dto);

    List<CarDto> toDtoList(List<Car> cars);
}

