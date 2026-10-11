package com.example.informationsystemslab1.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateDisciplineDto(
        @NotBlank String name
) {
}