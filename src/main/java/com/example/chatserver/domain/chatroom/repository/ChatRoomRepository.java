package com.example.chatserver.domain.chatroom.repository;

import com.example.chatserver.domain.chatroom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
}
