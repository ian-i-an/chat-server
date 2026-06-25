package com.example.chatserver.domain.room.repository;

import com.example.chatserver.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
