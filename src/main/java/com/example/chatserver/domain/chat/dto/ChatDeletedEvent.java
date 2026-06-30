package com.example.chatserver.domain.chat.dto;

public record ChatDeletedEvent(
        ChatDto chatDto,
        String roomCode
) {
}
