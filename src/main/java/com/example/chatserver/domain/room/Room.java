package com.example.chatserver.domain.room;


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
@Table(name = "room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends AuditingEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    private Room(String name, User owner) {
        validateName(name);

        this.name = name;
        this.owner = requireNonNull(owner, "방장은 필수입니다.");
    }

    public static Room create(String name, User owner) {
        return new Room(name, owner);
    }

    private static void validateName(String chatRoomName) {
        if(chatRoomName == null || chatRoomName.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
