package com.example.chatserver.domain.chat.dto;

public record ChatCreatedEvent(
        ChatDto chatDto,
        Long roomId
) {
}
