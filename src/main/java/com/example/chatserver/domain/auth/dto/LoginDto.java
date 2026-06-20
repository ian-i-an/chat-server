package com.example.chatserver.domain.auth.dto;

import com.example.chatserver.domain.user.dto.UserDto;

public record LoginDto(
        UserDto userDto,
        String accessToken,
        String refreshToken
) {
    }