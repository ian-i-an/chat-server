package com.example.chatserver.domain.readstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ReadStatus r " +
            "SET r.lastReadChatId = :chatId " +
            "WHERE r.chatRoom.id = :chatRoomId " +
            "AND r.user.id = :userId " +
            "AND (r.lastReadChatId IS NULL OR r.lastReadChatId < :chatId)")
    int updateLastReadChatIdIfGreater(
            @Param("chatRoomId") Long chatRoomId,
            @Param("userId") Long userId,
            @Param("chatId") Long chatId
    );

    Optional<ReadStatus> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
