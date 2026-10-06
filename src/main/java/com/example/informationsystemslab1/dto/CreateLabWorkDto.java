package com.example.informationsystemslab1.dto;

import com.example.informationsystemslab1.entity.enums.Difficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record CreateLabWorkDto(
        @NotBlank String name,
        @NotNull @Valid CoordinatesDto coordinates,
        @NotBlank String description,
        Difficulty difficulty,
        @Positive int minimalPoint,
        @NotNull @Positive Long personalQualitiesMaximum,
        int tunedInWork,
        @NotNull Long disciplineId,
        Set<Long> authorsIds
) {
}
