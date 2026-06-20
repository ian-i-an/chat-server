package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.request.ChatSendRequest;
import com.example.chatserver.domain.chat.dto.request.ReadRequest;
import com.example.chatserver.domain.readstatus.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final ChatService chatService;
    private final ReadStatusService readStatusService;


    @MessageMapping("/chat-rooms/{chatRoomId}/chats")
    public void sendChat(
            @DestinationVariable Long chatRoomId,
            @Payload ChatSendRequest chatSendRequest,
            Principal principal) {
        Long userId = null;

        if (principal != null) {
            userId = (Long) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        chatService.sendChat(chatRoomId, chatSendRequest, userId);
    }

    @MessageMapping("/chat-rooms/{chatRoomId}/read")
    public void updateReadStatus(
            @DestinationVariable Long chatRoomId,
            @Payload ReadRequest readRequest,
            Principal principal
    ) {
        Long userId = null;

        if (principal != null) {
            userId = (Long) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }

        readStatusService.updateLastReadChatId(
                chatRoomId,
                userId,
                readRequest.lastReadChatId()
        );
    }
}
