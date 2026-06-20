package com.example.chatserver.domain.chatroom;

import com.example.chatserver.domain.chatroom.dto.ChatRoomDto;
import com.example.chatserver.domain.chatroom.dto.ChatRoomListItem;
import com.example.chatserver.domain.chatroom.dto.request.ChatRoomCreateRequest;
import com.example.chatserver.domain.chatroom.repository.ChatRoomQueryRepository;
import com.example.chatserver.domain.chatroom.repository.ChatRoomRepository;
import com.example.chatserver.domain.readstatus.ReadStatus;
import com.example.chatserver.domain.readstatus.ReadStatusRepository;
import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomQueryRepository chatRoomQueryRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        ChatRoom chatRoom = ChatRoom.create(chatRoomCreateRequest.chatRoomName(), user);
        chatRoomRepository.save(chatRoom);
        readStatusRepository.save(ReadStatus.create(user, chatRoom));
    }

    public List<ChatRoomListItem> getChatRoomsByUserId(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        };
        return chatRoomQueryRepository.getChatRoomsByUserId(userId);
    }

    public ChatRoomDto getChatRoomById(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        boolean isMyChatRoom = chatRoom.getOwner().getId().equals(userId);
        return new ChatRoomDto(chatRoom.getId(), chatRoom.getName(), isMyChatRoom);
    }
}
