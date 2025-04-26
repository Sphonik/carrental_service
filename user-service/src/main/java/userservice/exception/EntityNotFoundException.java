// src/main/java/com/carrental/exception/EntityNotFoundException.java
package userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String entity, Object id) {
    super(entity + " not found with ID: " + id);
  }
}
