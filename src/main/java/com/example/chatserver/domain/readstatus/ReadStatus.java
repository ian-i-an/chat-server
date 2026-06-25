package com.example.chatserver.domain.readstatus;

import com.example.chatserver.domain.base.AuditingEntity;
import com.example.chatserver.domain.room.Room;
import com.example.chatserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static java.util.Objects.*;

@Entity
@Getter
@Table(name="read_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends AuditingEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "last_read_chat_id", nullable = false)
    private Long lastReadChatId;

    public static ReadStatus create(User user, Room room) {
        ReadStatus readStatus = new ReadStatus();
        readStatus.user = requireNonNull(user);
        readStatus.room = requireNonNull(room);
        readStatus.lastReadChatId = 0L;
        return readStatus;
    }

    public void updateLastReadChatId(Long lastReadChatId) {
        this.lastReadChatId = lastReadChatId;
    }
}