package com.example.chatserver.domain.auth;

import com.example.chatserver.domain.auth.dto.LoginDto;
import com.example.chatserver.domain.auth.dto.TokenDto;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.user.User;
import com.example.chatserver.domain.user.UserRepository;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.global.exception.BusinessException;
import com.example.chatserver.global.exception.ErrorCode;
import com.example.chatserver.global.security.JwtProvider;
import com.example.chatserver.global.utils.NicknameGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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
        if(refreshToken == null || refreshToken.isEmpty()) {
            throw new BusinessException(ErrorCode.LOGIN_REQUIRED );
        }

        Claims claims;
        try {
            claims = jwtProvider.parseClaims(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        if (!"refresh_token".equals(claims.get("type"))) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = Long.parseLong(claims.getSubject());

        String accessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);
        return new TokenDto( accessToken, newRefreshToken);
    }

    public void checkDuplicationId(String loginId) {
        boolean duplicated = userRepository.existsByLoginId(loginId);

        if (duplicated) {
            throw new BusinessException(ErrorCode.DUPLICATE_ID);
        }
    }

    @Transactional
    public void signUp(UserCreateRequest request) {
        boolean duplicated = userRepository.existsByLoginId(request.loginId());
        if (duplicated) {
            throw new BusinessException(ErrorCode.DUPLICATE_ID);
        }

        String randomNickname = NicknameGenerator.generate();
        String passwordHash = passwordEncoder.encode(request.password());
        User user = User.create(randomNickname, request.loginId(), passwordHash);

       userRepository.save(user);
    }

    public LoginDto signIn(LoginRequest request) {
        User user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        return new LoginDto(new UserDto(user.getId(), user.getNickname()), accessToken, refreshToken);
    }

    public UserDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return new UserDto(user.getId(), user.getNickname());
    }
}
