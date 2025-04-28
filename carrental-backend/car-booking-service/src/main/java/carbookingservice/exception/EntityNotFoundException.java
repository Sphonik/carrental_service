package carbookingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested entity cannot be found in the system.
 * <p>
 * Mapped to HTTP 404 Not Found by the {@link ResponseStatus} annotation.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

  /**
   * Constructs a new EntityNotFoundException for the given entity type and identifier.
   *
   * @param entity the name of the entity type (e.g., "Car", "Booking")
   * @param id     the identifier value that was not found
   */
  public EntityNotFoundException(String entity, Object id) {
    super(entity + " not found with ID: " + id);
  }
}
