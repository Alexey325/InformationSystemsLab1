package com.example.informationsystemslab1.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank String username,
        // TODO: ограничение BCrypt — пароль не длиннее 72 БАЙТ в UTF-8 (@Size считает символы, а не байты)
        @NotBlank String password
) {
}
