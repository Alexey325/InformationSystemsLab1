package com.example.informationsystemslab1.dto.request.create;

import jakarta.validation.constraints.NotBlank;

public record CreateDisciplineDto(
        @NotBlank String name
) {
}
