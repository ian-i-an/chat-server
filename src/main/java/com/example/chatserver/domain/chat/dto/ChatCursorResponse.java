package com.example.chatserver.domain.chat.dto;

import java.util.List;

public record ChatCursorResponse(
        List<ChatDto> chats,
        Boolean hasNext
) {
}
