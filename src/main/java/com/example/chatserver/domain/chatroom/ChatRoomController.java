package com.example.chatserver.domain.chatroom;

import com.example.chatserver.domain.chatroom.dto.ChatRoomDto;
import com.example.chatserver.domain.chatroom.dto.ChatRoomListItem;
import com.example.chatserver.domain.chatroom.dto.request.ChatRoomCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<Void> createChatRoom(
            @RequestBody ChatRoomCreateRequest chatRoomCreateRequest,
            @AuthenticationPrincipal Long userId) {
         chatRoomService.createChatRoom(chatRoomCreateRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomListItem>> getMyChatRooms(@AuthenticationPrincipal Long userId) {

        List<ChatRoomListItem> chatRoomList = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRoomList);
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDto> getChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Long userId
          ) {
        ChatRoomDto chatRoomDto = chatRoomService.getChatRoomById(chatRoomId, userId);
        return ResponseEntity.ok(chatRoomDto);
    }
}
