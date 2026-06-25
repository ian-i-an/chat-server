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


    @MessageMapping("/rooms/{roomId}/chats")
    public void sendChat(
            @DestinationVariable Long roomId,
            @Payload ChatSendRequest chatSendRequest,
            Principal principal) {
        Long userId = null;

        if (principal != null) {
            userId = (Long) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }
        chatService.sendChat(roomId, chatSendRequest, userId);
    }

    @MessageMapping("/rooms/{roomId}/read")
    public void updateReadStatus(
            @DestinationVariable Long roomId,
            @Payload ReadRequest readRequest,
            Principal principal
    ) {
        Long userId = null;

        if (principal != null) {
            userId = (Long) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }

        readStatusService.updateLastReadChatId(
                roomId,
                userId,
                readRequest.lastReadChatId()
        );
    }
}
