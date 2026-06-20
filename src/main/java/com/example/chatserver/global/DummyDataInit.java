package com.example.chatserver.global;

import com.example.chatserver.domain.auth.AuthService;
import com.example.chatserver.domain.auth.dto.LoginDto;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.chat.ChatService;
import com.example.chatserver.domain.chat.dto.request.ChatSendRequest;
import com.example.chatserver.domain.chatroom.ChatRoomService;

import com.example.chatserver.domain.chatroom.dto.ChatRoomListItem;
import com.example.chatserver.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 다른 방법들
 * 1. ApplicationRunner
 * 2. @PostConstruct
 * 3. @EventListener(ApplicationReadyEvent.class)
 * */
@Component
@RequiredArgsConstructor
public class DummyDataInit implements CommandLineRunner {

    private final AuthService authService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Override
    public void run(String... args) throws Exception {
        authService.signUp(new UserCreateRequest("test", "1234"));
        LoginDto loginDto = authService.signIn(new LoginRequest("test", "1234"));
        chatRoomService.createChatRoom(new ChatRoomCreateRequest("testRoom1"), loginDto.userDto().id());
        chatRoomService.createChatRoom(new ChatRoomCreateRequest("testRoom2"), loginDto.userDto().id());
        chatRoomService.createChatRoom(new ChatRoomCreateRequest("testRoom3"), loginDto.userDto().id());
        List<ChatRoomListItem> myChatRooms = chatRoomService.getChatRoomsByUserId(loginDto.userDto().id());
        ChatRoomListItem chatRoomListItem1 = myChatRooms.get(1);
//        chatService.
//        chatService.sendChat(new ChatSendRequest(chatRoomListItem1.id(),"test1 chat content"),  loginDto.userDto().id());

    }
}
