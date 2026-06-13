package com.example.chatserver.domain.auth.controller;

import com.example.chatserver.domain.auth.dto.TokenDto;
import com.example.chatserver.domain.auth.dto.request.IdCheckRequest;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.auth.service.AuthService;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.global.security.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/check-id")
    public ResponseEntity<Void> checkIdDuplication(@RequestBody IdCheckRequest request) {

        authService.checkDuplicationId(request.loginId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> register(@RequestBody UserCreateRequest userCreateRequest) {
        UserDto userDto = authService.signUp(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody LoginRequest request, HttpServletResponse response) {
        TokenDto tokenDto = authService.signIn(request);

        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", tokenDto.accessToken())
                .httpOnly(true)
                //https일 때만 요청에 담아서 보내라! -> 지금은 개발이니까 false
                .secure(false)
                .path("/")
                .maxAge(jwtProperties.getAccessTokenExpiration() / 1000)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenDto.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenExpiration() / 1000)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }
}
