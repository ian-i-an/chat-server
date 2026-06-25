package com.example.chatserver.domain.room;

import com.example.chatserver.domain.room.dto.RoomDto;
import com.example.chatserver.domain.room.dto.RoomListItem;
import com.example.chatserver.domain.room.dto.request.RoomCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<Void> createRoom(
            @RequestBody RoomCreateRequest roomCreateRequest,
            @AuthenticationPrincipal Long userId) {
         roomService.createRoom(roomCreateRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<RoomListItem>> getMyRooms(@AuthenticationPrincipal Long userId) {
        List<RoomListItem> chatRoomList = roomService.getRoomsByUserId(userId);
        return ResponseEntity.ok(chatRoomList);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Long userId
          ) {
        RoomDto roomDto = roomService.getRoomById(roomId, userId);
        return ResponseEntity.ok(roomDto);
    }
}
