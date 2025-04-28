package userservice.repository;

import userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities in MongoDB.
 * <p>
 * Provides methods to retrieve users by username and to check existence by username.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Retrieves a user by their unique username.
     *
     * @param username the login name of the user
     * @return an Optional containing the User if found, or empty if not
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user with the specified username already exists.
     *
     * @param username the login name to check
     * @return true if a user with the given username exists, false otherwise
     */
    boolean existsByUsername(String username);

}
