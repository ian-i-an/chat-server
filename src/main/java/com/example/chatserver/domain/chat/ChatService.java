package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCreatedEvent;
import com.example.chatserver.domain.chat.dto.ChatCursorResponse;
import com.example.chatserver.domain.chat.dto.ChatDto;
import com.example.chatserver.domain.chat.dto.ChatRoomUpdatedEvent;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import com.example.chatserver.domain.chat.dto.request.ChatSendRequest;
import com.example.chatserver.domain.chat.repository.ChatRepository;
import com.example.chatserver.domain.chatroom.ChatRoom;
import com.example.chatserver.domain.chatroom.repository.ChatRoomRepository;
import com.example.chatserver.domain.readstatus.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ReadStatusService readStatusService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
    public void sendChat(Long chatRoomId, ChatSendRequest chatSendRequest, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        Long isOwnerId = chatRoom.getOwner().getId();
        boolean isOwner = isOwnerId.equals(userId);
        Chat chat = Chat.create(chatSendRequest.content(), chatRoom, isOwner);
        Chat save = chatRepository.save(chat);

        if (isOwner) {
            readStatusService.updateLastReadChatId(chatRoomId, userId, save.getId());
        }

        ChatDto chatDto = new ChatDto(chat.getId(), chat.getContent(), chat.getCreatedAt(), chat.isOwnerChat());

        applicationEventPublisher.publishEvent(new ChatCreatedEvent(chatDto, chatRoomId));
        applicationEventPublisher.publishEvent(new ChatRoomUpdatedEvent(isOwnerId, chatRoomId, chat.getContent(), isOwner));

    }

    public ChatCursorResponse getChats(Long chatRoomId, ChatCursorCondition chatCursorCondition) {
        List<Chat> chats = chatRepository.getChatsByCursor(chatRoomId, chatCursorCondition);

        boolean hasNext = false;
        if (chats.size() == chatCursorCondition.limit() + 1) {
            chats = chats.subList(0, chatCursorCondition.limit());
            hasNext = true;
        }

        List<ChatDto> chatDtos = chats.stream().map(chat -> new ChatDto(chat.getId(), chat.getContent(), chat.getCreatedAt(), chat.isOwnerChat()))
                .toList();

        return new ChatCursorResponse(chatDtos, hasNext);
    }
}
