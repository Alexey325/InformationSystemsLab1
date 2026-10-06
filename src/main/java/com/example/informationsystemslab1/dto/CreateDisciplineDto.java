package com.example.informationsystemslab1.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDisciplineDto(
        @NotBlank String name
) {
}
