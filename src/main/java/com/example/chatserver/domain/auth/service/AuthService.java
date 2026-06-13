package com.example.chatserver.domain.auth.service;

import com.example.chatserver.domain.auth.dto.TokenDto;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.domain.user.repository.UserRepository;
import com.example.chatserver.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void checkDuplicationId(String loginId){
        boolean duplicated = userRepository.existsByLoginId(loginId);

        if(duplicated){
            throw new IllegalArgumentException("사용할 수 없는 아이디입니다.");
        }
    }

    public UserDto signUp(UserCreateRequest request) {
        User user = User.create(request.nickname(), request.loginId(), request.password());

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }

    public TokenDto signIn(LoginRequest request) {
        User user = userRepository.findByLoginId(request.loginId()).orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getNickname());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        return new TokenDto(accessToken, refreshToken);
    }

}
