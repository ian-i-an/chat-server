package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCursorResponse;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{roomCode}/chats")
    public ResponseEntity<ChatCursorResponse> getChats(
            @PathVariable String roomCode,
            @ModelAttribute ChatCursorCondition chatCursorCondition
    ) {
        ChatCursorResponse chatCursorResponse = chatService.getChats(roomCode, chatCursorCondition);
        return ResponseEntity.ok().body(chatCursorResponse);
    }

    @DeleteMapping("/{roomCode}/chats/{chatId}")
    public ResponseEntity<Void> deleteChat(
            @PathVariable String roomCode,
            @PathVariable Long chatId,
            @AuthenticationPrincipal Long userId) {
        chatService.deleteChat(roomCode, chatId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
