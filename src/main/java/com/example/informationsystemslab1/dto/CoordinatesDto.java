package com.example.informationsystemslab1.dto;

import jakarta.validation.constraints.NotNull;

public record CoordinatesDto(
        float x,
        @NotNull Integer y
) {
}
