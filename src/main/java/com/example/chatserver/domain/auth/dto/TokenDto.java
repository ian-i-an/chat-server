package com.example.chatserver.domain.auth.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
