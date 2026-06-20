package com.example.chatserver.domain.user.dto.request;

public record UserCreateRequest(
        String loginId,
        String password
) {
}
