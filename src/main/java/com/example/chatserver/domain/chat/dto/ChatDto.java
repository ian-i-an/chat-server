package com.example.chatserver.domain.chat.dto;

import java.time.Instant;

public record ChatDto(
        Long id,
        String content,
        Instant createdAt,
        Boolean isOwner
) {
}
