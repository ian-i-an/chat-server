package com.example.chatserver.domain.chatroom;


import com.example.chatserver.domain.base.AuditingEntity;
import com.example.chatserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@Table(name = "chat_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends AuditingEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    private ChatRoom(String name, User owner) {
        validateName(name);

        this.name = name;
        this.owner = requireNonNull(owner, "방장은 필수입니다.");
    }

    public static ChatRoom create(String name, User owner) {
        return new ChatRoom(name, owner);
    }

    public static void validateName(String chatRoomName) {
        if(chatRoomName == null || chatRoomName.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
