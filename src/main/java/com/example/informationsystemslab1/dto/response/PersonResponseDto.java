package com.example.informationsystemslab1.dto.response;

import com.example.informationsystemslab1.dto.ref.LabWorkRefDto;
import com.example.informationsystemslab1.dto.ref.LocationRefDto;
import com.example.informationsystemslab1.enums.Color;
import com.example.informationsystemslab1.enums.Country;

import java.util.Set;

public record PersonResponseDto(
        Long id,
        String name,
        Color eyeColor,
        Color hairColor,
        LocationRefDto location,
        String passportID,
        Country nationality,
        Set<LabWorkRefDto> labWorks
) {
}
