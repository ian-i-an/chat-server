package com.example.chatserver.domain.chatroom.dto;

public record ChatRoomListItem(
        Long id,
        String name,
        String lastMessage,
        Long unreadCount
) {
}
