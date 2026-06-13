package com.example.chatserver.domain.auth.dto.request;

public record LoginRequest(
        String loginId,
        String password
) {
}
