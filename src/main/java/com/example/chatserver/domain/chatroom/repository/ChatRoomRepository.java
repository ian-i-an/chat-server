package com.example.chatserver.domain.chatroom.repository;

import com.example.chatserver.domain.chatroom.ChatRoom;
import com.example.chatserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    List<ChatRoom> findChatRoomsByOwner(User owner);
}
