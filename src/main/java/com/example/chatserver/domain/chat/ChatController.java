package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCursorResponse;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{roomId}/chats")
    public ResponseEntity<ChatCursorResponse> getChats(
            @PathVariable Long roomId,
            @ModelAttribute ChatCursorCondition chatCursorCondition
    ) {
        ChatCursorResponse chatCursorResponse = chatService.getChats(roomId, chatCursorCondition);
        return ResponseEntity.ok().body(chatCursorResponse);
    }
}
