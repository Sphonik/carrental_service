package carbookingservice.mapper;

import carbookingservice.dto.CarDto;
import carbookingservice.model.Car;
import carbookingservice.model.FuelType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-27T16:37:09+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class CarMapperImpl implements CarMapper {

    @Override
    public CarDto toDto(Car car) {
        if ( car == null ) {
            return null;
        }

        String id = null;
        String make = null;
        String model = null;
        Integer year = null;
        String color = null;
        String fuelType = null;
        boolean automatic = false;
        BigDecimal pricePerDay = null;
        String pickupLocation = null;
        boolean available = false;

        id = car.getId();
        make = car.getMake();
        model = car.getModel();
        year = car.getYear();
        color = car.getColor();
        if ( car.getFuelType() != null ) {
            fuelType = car.getFuelType().name();
        }
        automatic = car.isAutomatic();
        pricePerDay = car.getPricePerDay();
        pickupLocation = car.getPickupLocation();
        available = car.isAvailable();

        CarDto carDto = new CarDto( id, make, model, year, color, fuelType, automatic, pricePerDay, pickupLocation, available );

        return carDto;
    }

    @Override
    public Car toEntity(CarDto dto) {
        if ( dto == null ) {
            return null;
        }

        Car car = new Car();

        car.setId( dto.id() );
        car.setMake( dto.make() );
        car.setModel( dto.model() );
        car.setYear( dto.year() );
        car.setColor( dto.color() );
        if ( dto.fuelType() != null ) {
            car.setFuelType( Enum.valueOf( FuelType.class, dto.fuelType() ) );
        }
        car.setAutomatic( dto.automatic() );
        car.setPricePerDay( dto.pricePerDay() );
        car.setPickupLocation( dto.pickupLocation() );
        car.setAvailable( dto.available() );

        return car;
    }

    @Override
    public List<CarDto> toDtoList(List<Car> cars) {
        if ( cars == null ) {
            return null;
        }

        List<CarDto> list = new ArrayList<CarDto>( cars.size() );
        for ( Car car : cars ) {
            list.add( toDto( car ) );
        }

        return list;
    }
}
