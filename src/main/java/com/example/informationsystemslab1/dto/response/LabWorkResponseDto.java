package com.example.informationsystemslab1.dto.response;

import com.example.informationsystemslab1.dto.CoordinatesDto;
import com.example.informationsystemslab1.dto.ref.DisciplineRefDto;
import com.example.informationsystemslab1.dto.ref.PersonRefDto;
import com.example.informationsystemslab1.entity.enums.Difficulty;

import java.time.LocalDate;
import java.util.Set;

public record LabWorkResponseDto(
        Long id,
        String name,
        CoordinatesDto coordinates,
        LocalDate creationDate,
        String description,
        Difficulty difficulty,
        DisciplineRefDto discipline,
        int minimalPoint,
        Long personalQualitiesMaximum,
        int tunedInWork,
        Set<PersonRefDto> authors
) {
}
