package com.example.informationsystemslab1.mapper;

import com.example.informationsystemslab1.dto.ref.LabWorkRefDto;
import com.example.informationsystemslab1.dto.response.DisciplineResponseDto;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.entity.LabWork;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DisciplineMapper {
    DisciplineResponseDto toDto(Discipline discipline);

    LabWorkRefDto toRefDto(LabWork labWork);

}
