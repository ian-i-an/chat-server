package com.example.chatserver.domain.chat.dto;

public record ChatRoomUpdatedEvent(
        Long userId,
        Long chatRoomId,
        String lastMessage,
        boolean isMyMessage
) {
}
