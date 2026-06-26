package com.example.chatserver.domain.room.dto;

public record RoomDto(
       String roomCode,
        String name,
        Boolean isMyRoom
) {
}
