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


    @MessageMapping("/rooms/{roomCode}/chats")
    public void sendChat(
            @DestinationVariable String roomCode,
            @Payload ChatSendRequest chatSendRequest,
            Principal principal) {
        Long userId = null;

        if (principal != null) {
            userId = (Long) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        chatService.sendChat(roomCode, chatSendRequest, userId);
    }

    @MessageMapping("/rooms/{roomCode}/read")
    public void updateReadStatus(
            @DestinationVariable String roomCode,
            @Payload ReadRequest readRequest,
            Principal principal
    ) {
        Long userId = null;

        if (principal != null) {
            userId = (Long) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }

        readStatusService.updateLastReadChatId(
                roomCode,
                userId,
                readRequest.lastReadChatId()
        );
    }
}
