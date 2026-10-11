package com.example.informationsystemslab1.mapper;

import com.example.informationsystemslab1.dto.CoordinatesDto;
import com.example.informationsystemslab1.dto.ref.DisciplineRefDto;
import com.example.informationsystemslab1.dto.ref.PersonRefDto;
import com.example.informationsystemslab1.dto.response.LabWorkResponseDto;
import com.example.informationsystemslab1.entity.Coordinates;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.entity.LabWork;
import com.example.informationsystemslab1.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LabWorkMapper {
    LabWorkResponseDto toDto(LabWork labWork);

    CoordinatesDto toDto(Coordinates coordinates);

    DisciplineRefDto toRefDto(Discipline discipline);

    PersonRefDto toRefDto(Person person);
}
