package userservice.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import userservice.dto.CreateUserRequestDto;
import userservice.dto.UserDto;
import userservice.model.User;
import userservice.model.UserRole;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-27T12:48:13+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        String id = null;
        String firstName = null;
        String lastName = null;
        String username = null;
        String userRole = null;

        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        username = user.getUsername();
        if ( user.getUserRole() != null ) {
            userRole = user.getUserRole().name();
        }

        UserDto userDto = new UserDto( id, firstName, lastName, username, userRole );

        return userDto;
    }

    @Override
    public User toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.id() );
        user.setFirstName( userDto.firstName() );
        user.setLastName( userDto.lastName() );
        user.setUsername( userDto.username() );
        if ( userDto.userRole() != null ) {
            user.setUserRole( Enum.valueOf( UserRole.class, userDto.userRole() ) );
        }

        return user;
    }

    @Override
    public List<UserDto> toDtoList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public User toEntity(CreateUserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( dto.firstName() );
        user.setLastName( dto.lastName() );
        user.setUsername( dto.username() );

        user.setUserRole( UserRole.valueOf(dto.userRole()) );

        return user;
    }
}
