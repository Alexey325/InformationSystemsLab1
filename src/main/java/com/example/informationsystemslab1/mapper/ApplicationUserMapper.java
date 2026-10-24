package com.example.informationsystemslab1.mapper;

import com.example.informationsystemslab1.dto.response.UserResponseDto;
import com.example.informationsystemslab1.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApplicationUserMapper {
    UserResponseDto toDto(ApplicationUser user);
}
