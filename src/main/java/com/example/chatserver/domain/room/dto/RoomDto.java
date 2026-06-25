package com.example.chatserver.domain.room.dto;

public record RoomDto(
        Long id,
        String name,
        Boolean isMyRoom
) {
}
