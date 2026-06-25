package com.example.chatserver.domain.chat.dto.request;

import jakarta.validation.constraints.Min;

public record ChatCursorCondition(
        Long cursor,
        @Min(1)
        int limit
) {
}
