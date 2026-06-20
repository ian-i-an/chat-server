package com.example.chatserver.domain.user;

import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUserById(Long userId){
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserDto updateUser(Long userId, UserUpdateRequest userUpdateRequest){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.changeNickname(userUpdateRequest.newNickname());
        return new UserDto(user.getId(), user.getNickname());
    }
}
