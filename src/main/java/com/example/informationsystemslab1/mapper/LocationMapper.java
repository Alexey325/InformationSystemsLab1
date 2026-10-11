package com.example.informationsystemslab1.mapper;

import com.example.informationsystemslab1.dto.ref.PersonRefDto;
import com.example.informationsystemslab1.dto.response.LocationResponseDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LocationMapper {

    LocationResponseDto toDto(Location location);

    PersonRefDto toRefDto(Person person);
}
