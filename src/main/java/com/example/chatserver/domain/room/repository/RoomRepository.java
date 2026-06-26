package com.example.chatserver.domain.room.repository;

import com.example.chatserver.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    boolean existsByCode(String roomCode);
    Optional<Room> findByCode(String roomCode);
}
