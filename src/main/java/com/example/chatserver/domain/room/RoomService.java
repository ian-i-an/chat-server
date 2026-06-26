package com.example.chatserver.domain.room;

import com.example.chatserver.domain.room.dto.RoomDto;
import com.example.chatserver.domain.room.dto.RoomListItem;
import com.example.chatserver.domain.room.dto.request.RoomCreateRequest;
import com.example.chatserver.domain.room.repository.RoomQueryRepository;
import com.example.chatserver.domain.room.repository.RoomRepository;
import com.example.chatserver.domain.readstatus.ReadStatus;
import com.example.chatserver.domain.readstatus.ReadStatusRepository;
import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.UserRepository;
import com.example.chatserver.global.exception.BusinessException;
import com.example.chatserver.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomQueryRepository roomQueryRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createRoom(RoomCreateRequest roomCreateRequest, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String code;
        do {
            code = RoomCodeGenerator.generateRoomCode();
        } while (roomRepository.existsByCode(code));

        Room room = Room.create(roomCreateRequest.roomName(), user, code);
        roomRepository.save(room);
        readStatusRepository.save(ReadStatus.create(user, room));
    }

    public List<RoomListItem> getRoomsByUserId(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        };
        return roomQueryRepository.getRoomsByUserId(userId);
    }

    public RoomDto getRoomByCode(String roomCode, Long userId) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() ->  new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        boolean isMyRoom = room.getOwner().getId().equals(userId);
        return new RoomDto(room.getCode(), room.getName(), isMyRoom);
    }
}
