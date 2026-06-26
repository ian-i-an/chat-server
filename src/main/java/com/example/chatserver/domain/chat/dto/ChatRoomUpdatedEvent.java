package com.example.chatserver.domain.chat.dto;

public record ChatRoomUpdatedEvent(
        Long userId,
        String roomCode,
        String lastMessage,
        boolean isMyMessage
) {
}
