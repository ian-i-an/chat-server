package com.example.chatserver.domain.room.dto;

public record RoomListItem(
        String roomCode,
        String name,
        String lastMessage,
        Long unreadCount
) {
}
