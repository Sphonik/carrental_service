package com.carrental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an entity of a given type with the specified ID cannot be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

  /**
   * Constructs a new EntityNotFoundException with a detail message
   * indicating the entity type and the missing ID.
   *
   * @param entity name of the entity type (e.g., "User", "Car", "Booking")
   * @param id     identifier of the entity that was not found
   */
  public EntityNotFoundException(String entity, Object id) {
    super(entity + " not found with ID: " + id);
  }
}
