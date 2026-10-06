package com.example.informationsystemslab1.dto;

import com.example.informationsystemslab1.entity.enums.Color;
import com.example.informationsystemslab1.entity.enums.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePersonDto(
        @NotBlank String name,
        Color eyeColor,
        Color hairColor,
        @NotNull Long locationId,
        @NotBlank @Size(max = 24) String passportID,
        @NotNull Country nationality
) {
}
