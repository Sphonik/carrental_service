package userservice.dto;

import java.io.Serializable;

public record UserRequestDto(
        String id
) implements Serializable {}
