package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCreatedEvent;
import com.example.chatserver.domain.chat.dto.ChatRoomUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChatWebSocketBroadcaster {
    private final SimpMessagingTemplate messagingTemplate;

    @Async("ChatSendTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendChat(ChatCreatedEvent event) {
        String destination = "/sub/rooms/" + event.chatRoomId();
        messagingTemplate.convertAndSend(destination, event.chatDto());
    }

    @Async("ChatSendTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateChatRoomList(ChatRoomUpdatedEvent event) {
        String destination = "/sub/users/" + event.userId() + "/rooms";
        messagingTemplate.convertAndSend(destination, new RoomUpdateEvent(event.chatRoomId(), event.lastMessage(), event.isMyMessage()));
    }

    record RoomUpdateEvent(
            Long id,
            String lastMessage,
            boolean isMyMessage
    ){}
}
