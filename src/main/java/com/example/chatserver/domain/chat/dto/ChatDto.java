package com.example.chatserver.domain.chat.dto;

import com.example.chatserver.domain.chat.Chat;

import java.time.Instant;

public record ChatDto(
        Long id,
        String content,
        Instant createdAt,
        Boolean isOwner,
        Boolean isDeleted,
        ReplyTo replyTo

) {
    public static ChatDto from(Chat chat) {

        return new ChatDto(
                chat.getId(),
                chat.isDeleted() ? null : chat.getContent(),
                chat.getCreatedAt(),
                chat.isOwnerChat(),
                chat.isDeleted(),
                chat.getReplyTo() != null ? ReplyTo.from(chat.getReplyTo()) : null);
    }
}
