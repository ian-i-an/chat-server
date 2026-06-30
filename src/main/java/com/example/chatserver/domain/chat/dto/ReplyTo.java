package com.example.chatserver.domain.chat.dto;


import com.example.chatserver.domain.chat.Chat;



public record ReplyTo(
        Long id,
        String content
) {
    public static ReplyTo from(Chat chat){
        return new ReplyTo(chat.getId(),
                chat.isDeleted()? null: chat.getContent());
    }
}
