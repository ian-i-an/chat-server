package com.example.chatserver.domain.chat;

import com.example.chatserver.domain.chat.dto.*;
import com.example.chatserver.domain.chat.dto.request.ChatCursorCondition;
import com.example.chatserver.domain.chat.dto.request.ChatSendRequest;
import com.example.chatserver.domain.chat.repository.ChatRepository;
import com.example.chatserver.domain.readstatus.ReadStatusService;
import com.example.chatserver.domain.room.Room;
import com.example.chatserver.domain.room.repository.RoomRepository;
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

        Chat replyTo = null;
        if (chatSendRequest.replyToId() != null) {
            replyTo = chatRepository.findById(chatSendRequest.replyToId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_NOT_FOUND));
            if (!replyTo.getRoom().getId().equals(room.getId())) {
                throw new BusinessException(ErrorCode.CHAT_NOT_IN_ROOM);
            }
        }

        Chat chat = Chat.create(chatSendRequest.content(), room, isOwner, replyTo);
        Chat save = chatRepository.save(chat);

        if (isOwner) {
            readStatusService.updateLastReadChatId(roomCode, userId, save.getId());
        }

        ChatDto chatDto = ChatDto.from(chat);

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

        List<ChatDto> chatDtos = chats.stream().map(ChatDto::from)
                .toList();

        return new ChatCursorResponse(chatDtos, hasNext);
    }

    @Transactional
    public void deleteChat(String roomCode, Long chatId, Long userId) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        if(!room.getOwner().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.REQUIRED_AUTH);
        }

        Chat chat = chatRepository.findByIdAndRoomId(chatId, room.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_NOT_FOUND));

        chat.delete();

        applicationEventPublisher.publishEvent(new ChatDeletedEvent(ChatDto.from(chat), roomCode));
    }
}
