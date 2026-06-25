package com.example.chatserver.domain.chat;


import com.example.chatserver.domain.base.AuditingEntity;
import com.example.chatserver.domain.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "chat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends AuditingEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @Column(nullable = false)
    private boolean isOwnerChat;

    public static Chat create(String content, Room room, boolean isOwnerChat) {
        validateContent(content);

        Chat chat = new Chat();
        chat.content = requireNonNull(content);
        chat.room = requireNonNull(room, "채팅방 정보는 필수입니다.");
        chat.isOwnerChat = isOwnerChat;

        return chat;
    }

    private static void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 비어있을 수 없습니다.");
        }
    }
}