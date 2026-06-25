package com.example.chatserver.domain.room.dto;

public record RoomListItem(
        Long id,
        String name,
        String lastMessage,
        Long unreadCount
) {
}
