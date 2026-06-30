package com.example.chatserver.domain.chat.dto;

public record ChatEventPayload(
        ChatEventType type,
        ChatDto chat
) {
    public static ChatEventPayload created(ChatDto chat) {
        return new ChatEventPayload(ChatEventType.CREATED, chat);
    }

    public static ChatEventPayload deleted(ChatDto chat) {
        return new ChatEventPayload(ChatEventType.DELETED, chat);
    }
}
