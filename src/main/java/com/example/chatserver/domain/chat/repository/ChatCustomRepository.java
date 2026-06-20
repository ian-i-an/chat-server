package com.example.chatserver.domain.chat.repository;

import com.example.chatserver.domain.chat.Chat;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;

import java.util.List;

public interface ChatCustomRepository {

    List<Chat> getChatsByCursor(Long chatRoomId, ChatCursorCondition chatCursorCondition);
}
