package com.example.chatserver.domain.user.service;

import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}
