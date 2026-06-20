package com.example.chatserver.domain.chat.dto.request;

public record ReadRequest(
        Long lastReadChatId
) {
}
