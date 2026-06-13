package com.example.chatserver.domain.chatroom.controller;

import com.example.chatserver.domain.chatroom.dto.ChatRoomDto;
import com.example.chatserver.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.chatserver.domain.chatroom.service.ChatRoomService;
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
    public ResponseEntity<ChatRoomDto>  createChatRoom(
            @RequestBody ChatRoomCreateRequest chatRoomCreateRequest,
            @AuthenticationPrincipal Long userId) {
        ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(chatRoomCreateRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomDto);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDto>>  getMyChatRooms(@AuthenticationPrincipal Long userId) {
        List<ChatRoomDto> chatRooms = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }
}
