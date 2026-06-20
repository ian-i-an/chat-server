package com.example.chatserver.domain.auth;

import com.example.chatserver.domain.auth.dto.LoginDto;
import com.example.chatserver.domain.auth.dto.TokenDto;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.UserRepository;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.global.security.JwtProvider;
import com.example.chatserver.global.utils.NicknameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokenDto refreshToken(String refreshToken) {
        Long userId = jwtProvider.getUserIdFromToken(refreshToken);

        String accessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);
        return new TokenDto( accessToken, newRefreshToken);
    }

    public void checkDuplicationId(String loginId) {
        boolean duplicated = userRepository.existsByLoginId(loginId);

        if (duplicated) {
            throw new IllegalArgumentException("사용할 수 없는 아이디입니다.");
        }
    }

    @Transactional
    public void signUp(UserCreateRequest request) {
        boolean duplicated = userRepository.existsByLoginId(request.loginId());
        if (duplicated) {
            throw new IllegalArgumentException("사용할 수 없는 아이디입니다.");
        }

        String randomNickname = NicknameGenerator.generate();
        String passwordHash = passwordEncoder.encode(request.password());
        User user = User.create(randomNickname, request.loginId(), passwordHash);

       userRepository.save(user);
    }

    public LoginDto signIn(LoginRequest request) {
        User user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        return new LoginDto(new UserDto(user.getId(), user.getNickname()), accessToken, refreshToken);
    }

    public UserDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 존재하지 않습니다."));

        return new UserDto(user.getId(), user.getNickname());
    }
}
