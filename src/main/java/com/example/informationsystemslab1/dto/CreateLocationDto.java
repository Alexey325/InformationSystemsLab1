package com.example.informationsystemslab1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateLocationDto(
        @NotNull Float x,
        @NotNull Long y,
        @NotNull Integer z,
        @Pattern(regexp = ".*\\S.*", message = "name must not be blank if present") String name
) {
}
