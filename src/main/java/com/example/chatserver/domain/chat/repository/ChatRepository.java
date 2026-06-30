package com.example.chatserver.domain.chat.repository;

import com.example.chatserver.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatCustomRepository {
    boolean existsByIdAndRoomId(Long chatId, Long roomId);
    Optional<Chat> findByIdAndRoomId(Long chatId, Long roomId);
}
