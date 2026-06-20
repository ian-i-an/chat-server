package com.example.chatserver.domain.chat.repository;

import com.example.chatserver.domain.chat.Chat;
import com.example.chatserver.domain.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatCustomRepository {
    boolean existsByIdAndChatRoomId(Long chatId, Long chatRoomId);
}
