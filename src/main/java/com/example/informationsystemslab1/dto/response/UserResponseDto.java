package com.example.informationsystemslab1.dto.response;

import com.example.informationsystemslab1.enums.Role;

public record UserResponseDto(
        Long id,
        String username,
        Role role
) {
}
