package com.example.informationsystemslab1.mapper;

import com.example.informationsystemslab1.dto.ref.LabWorkRefDto;
import com.example.informationsystemslab1.dto.ref.LocationRefDto;
import com.example.informationsystemslab1.dto.response.PersonResponseDto;
import com.example.informationsystemslab1.entity.LabWork;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PersonMapper {

    PersonResponseDto toDto(Person person);

    LocationRefDto toRefDto(Location location);

    LabWorkRefDto toRefDto(LabWork labWork);
}
