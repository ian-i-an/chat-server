package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.ChatCreatedEvent;
import com.example.chatserver.domain.chat.dto.ChatCursorResponse;
import com.example.chatserver.domain.chat.dto.ChatDto;
import com.example.chatserver.domain.chat.dto.ChatRoomUpdatedEvent;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import com.example.chatserver.domain.chat.dto.request.ChatSendRequest;
import com.example.chatserver.domain.chat.repository.ChatRepository;
import com.example.chatserver.domain.room.Room;
import com.example.chatserver.domain.room.repository.RoomRepository;
import com.example.chatserver.domain.readstatus.ReadStatusService;
import com.example.chatserver.global.exception.BusinessException;
import com.example.chatserver.global.exception.ErrorCode;
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
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
    public void sendChat(String roomCode, ChatSendRequest chatSendRequest, Long userId) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        Long isOwnerId = room.getOwner().getId();
        boolean isOwner = isOwnerId.equals(userId);
        Chat chat = Chat.create(chatSendRequest.content(), room, isOwner);
        Chat save = chatRepository.save(chat);

        if (isOwner) {
            readStatusService.updateLastReadChatId(roomCode, userId, save.getId());
        }

        ChatDto chatDto = new ChatDto(chat.getId(), chat.getContent(), chat.getCreatedAt(), chat.isOwnerChat());

        applicationEventPublisher.publishEvent(new ChatCreatedEvent(chatDto, roomCode));
        applicationEventPublisher.publishEvent(new ChatRoomUpdatedEvent(isOwnerId, roomCode, chat.getContent(), isOwner));

    }

    public ChatCursorResponse getChats(String roomCode, ChatCursorCondition chatCursorCondition) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        List<Chat> chats = chatRepository.getChatsByCursor(room.getId(), chatCursorCondition);

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
