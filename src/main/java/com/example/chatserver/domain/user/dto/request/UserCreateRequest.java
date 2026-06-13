package com.example.chatserver.domain.user.dto.request;

public record UserCreateRequest(
        String nickname,
        String loginId,
        String password
) {
}
