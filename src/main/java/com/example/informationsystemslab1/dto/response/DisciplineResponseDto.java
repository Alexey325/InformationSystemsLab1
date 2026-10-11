package com.example.informationsystemslab1.dto.response;

import com.example.informationsystemslab1.dto.ref.LabWorkRefDto;

import java.util.List;

public record DisciplineResponseDto(
        Long id,
        String name,
        int labsCount,
        List<LabWorkRefDto> labWorks
) {

}
