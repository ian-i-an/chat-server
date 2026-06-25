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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Room room = Room.create(roomCreateRequest.roomName(), user);
        roomRepository.save(room);
        readStatusRepository.save(ReadStatus.create(user, room));
    }

    public List<RoomListItem> getRoomsByUserId(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        };
        return roomQueryRepository.getRoomsByUserId(userId);
    }

    public RoomDto getRoomById(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        boolean isMyRoom = room.getOwner().getId().equals(userId);
        return new RoomDto(room.getId(), room.getName(), isMyRoom);
    }
}
