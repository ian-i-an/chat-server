package com.example.chatserver.domain.readstatus;

import com.example.chatserver.domain.chat.repository.ChatRepository;
import com.example.chatserver.domain.room.Room;
import com.example.chatserver.domain.room.repository.RoomRepository;
import com.example.chatserver.global.exception.BusinessException;
import com.example.chatserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStatusService {

    private final  ReadStatusRepository readStatusRepository;
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public void updateLastReadChatId(String roomCode, Long userId, Long lastReadChatId) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        boolean isChatInRoom = chatRepository.existsByIdAndRoomId(lastReadChatId, room.getId());

        if (!isChatInRoom) {
            throw new BusinessException(ErrorCode.CHAT_NOT_IN_ROOM);
        }
        readStatusRepository.updateLastReadChatIdIfGreater(room.getId(), userId, lastReadChatId);
    }
}
