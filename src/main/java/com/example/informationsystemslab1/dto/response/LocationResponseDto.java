package com.example.informationsystemslab1.dto.response;

import com.example.informationsystemslab1.dto.ref.PersonRefDto;

import java.util.List;

public record LocationResponseDto(
        Long id,
        Float x,
        Long y,
        Integer z,
        String name,
        List<PersonRefDto> people
) {
}
