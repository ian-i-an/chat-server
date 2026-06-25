package com.example.chatserver.global;

import com.example.chatserver.domain.auth.AuthService;
import com.example.chatserver.domain.auth.dto.LoginDto;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.chat.ChatService;
import com.example.chatserver.domain.room.RoomService;

import com.example.chatserver.domain.room.dto.RoomListItem;
import com.example.chatserver.domain.room.dto.request.RoomCreateRequest;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 다른 방법들
 * 1. ApplicationRunner
 * 2. @PostConstruct
 * 3. @EventListener(ApplicationReadyEvent.class)
 * */
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DummyDataInit implements CommandLineRunner {

    private final AuthService authService;
    private final RoomService roomService;
    private final ChatService chatService;

    @Override
    public void run(String... args) throws Exception {
        authService.signUp(new UserCreateRequest("test", "1234"));
        LoginDto loginDto = authService.signIn(new LoginRequest("test", "1234"));
        roomService.createRoom(new RoomCreateRequest("testRoom1"), loginDto.userDto().id());
        roomService.createRoom(new RoomCreateRequest("testRoom2"), loginDto.userDto().id());
        roomService.createRoom(new RoomCreateRequest("testRoom3"), loginDto.userDto().id());
        List<RoomListItem> myChatRooms = roomService.getRoomsByUserId(loginDto.userDto().id());

//        chatService.
//        chatService.sendChat(new ChatSendRequest(chatRoomListItem1.id(),"test1 chat content"),  loginDto.userDto().id());

    }
}
