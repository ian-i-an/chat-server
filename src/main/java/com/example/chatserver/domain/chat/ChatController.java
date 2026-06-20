package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCursorResponse;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{chatRoomId}/chats")
    public ResponseEntity<ChatCursorResponse> getChats(
            @PathVariable Long chatRoomId,
            @ModelAttribute ChatCursorCondition chatCursorCondition
    ) {
        ChatCursorResponse chatCursorResponse = chatService.getChats(chatRoomId, chatCursorCondition);
        return ResponseEntity.ok().body(chatCursorResponse);
    }
}
