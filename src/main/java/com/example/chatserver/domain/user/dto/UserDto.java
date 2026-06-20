package com.example.chatserver.domain.user.dto;

import com.example.chatserver.domain.user.User;

public record UserDto(
        Long id,
        String nickname
) {
}
