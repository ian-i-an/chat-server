package com.example.chatserver.domain.readstatus;

import com.example.chatserver.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStatusService {

    private final  ReadStatusRepository readStatusRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public void updateLastReadChatId(Long chatRoomId, Long userId, Long lastReadChatId) {
        boolean isChatInRoom = chatRepository.existsByIdAndChatRoomId(lastReadChatId, chatRoomId);

        if (!isChatInRoom) {
            throw new IllegalArgumentException("채팅이 존재하지 않습니다.");
        }
        readStatusRepository.updateLastReadChatIdIfGreater(chatRoomId, userId, lastReadChatId);
    }
}
