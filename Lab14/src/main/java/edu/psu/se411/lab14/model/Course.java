package edu.psu.se411.lab14.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;

public record Course(
        @Id @NotNull @PositiveOrZero Long id,
        @NotBlank String code,
        @NotBlank @Size(min = 2) String name,
        String description,
        @NotNull @Min(1) @Max(10) Integer credits) {
}
