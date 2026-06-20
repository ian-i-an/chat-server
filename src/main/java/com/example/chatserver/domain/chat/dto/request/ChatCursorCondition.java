package com.example.chatserver.domain.chat.dto.request;

public record ChatCursorCondition(
        Long cursor,
        int limit
) {
}
