package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCreatedEvent;
import com.example.chatserver.domain.chat.dto.ChatDeletedEvent;
import com.example.chatserver.domain.chat.dto.ChatEventPayload;
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
        String destination = "/sub/rooms/" + event.roomCode();
        messagingTemplate.convertAndSend(destination,
                ChatEventPayload.created(event.chatDto()));
    }

    @Async("ChatSendTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteChat(ChatDeletedEvent event) {
        String destination = "/sub/rooms/" + event.roomCode();
        messagingTemplate.convertAndSend(destination,ChatEventPayload.deleted(event.chatDto()));
    }

    @Async("ChatSendTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateChatRoomList(ChatRoomUpdatedEvent event) {
        String destination = "/sub/users/" + event.userId() + "/rooms";
        messagingTemplate.convertAndSend(destination, new RoomUpdateEvent(event.roomCode(), event.lastMessage(), event.isMyMessage()));
    }

    record RoomUpdateEvent(
            String roomCode,
            String lastMessage,
            boolean isMyMessage
    ){}
}
