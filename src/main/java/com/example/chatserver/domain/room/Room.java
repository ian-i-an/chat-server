package com.example.chatserver.domain.room;


import com.example.chatserver.domain.base.AuditingEntity;
import com.example.chatserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static com.example.chatserver.domain.room.RoomCodeGenerator.*;
import static java.util.Objects.requireNonNull;

@Getter
@Entity
@Table(name = "room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends AuditingEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;


    public static Room create(String name, User owner, String code) {
        Room room = new Room();
        validateName(name);

        room.name = name;
        room.owner = requireNonNull(owner, "방장은 필수입니다.");
        room.code = code;

        return room;
    }

    private static void validateName(String chatRoomName) {
        if(chatRoomName == null || chatRoomName.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
