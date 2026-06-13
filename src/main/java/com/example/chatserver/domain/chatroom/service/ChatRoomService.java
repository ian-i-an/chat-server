package com.example.chatserver.domain.chatroom.service;

import com.example.chatserver.domain.chatroom.ChatRoom;
import com.example.chatserver.domain.chatroom.dto.ChatRoomDto;
import com.example.chatserver.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.chatserver.domain.chatroom.repository.ChatRoomRepository;
import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomDto createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest, Long userId){

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        ChatRoom chatRoom = ChatRoom.create(chatRoomCreateRequest.chatRoomName(), user);
        chatRoomRepository.save(chatRoom);
        return new ChatRoomDto(chatRoom.getId(), chatRoom.getName());
    }

    public List<ChatRoomDto> getChatRoomsByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUser(user);
        return chatRooms
                .stream()
                .map(chatRoom -> new ChatRoomDto(chatRoom.getId(), chatRoom.getName())).toList();
    }
}
