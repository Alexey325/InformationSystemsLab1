package com.example.informationsystemslab1.dto.request.auth;

import com.example.informationsystemslab1.validation.MaxBytes;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank String username,
        // BCrypt принимает не больше 72 байт(кириллица 2 байта на символ)
        @NotBlank @MaxBytes(max = 72) String password
) {
}
