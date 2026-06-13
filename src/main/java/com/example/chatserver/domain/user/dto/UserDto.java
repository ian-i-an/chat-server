package com.example.chatserver.domain.user.dto;

import com.example.chatserver.domain.user.User;

public record UserDto(
        Long id,
        String nickname
) {
    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getNickname());
    }
}
