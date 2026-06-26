package com.example.chatserver.domain.auth;

import com.example.chatserver.domain.auth.dto.LoginDto;
import com.example.chatserver.domain.auth.dto.TokenDto;
import com.example.chatserver.domain.auth.dto.request.IdCheckRequest;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.global.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.chatserver.domain.auth.CookieCreateHelper.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;
    @Value("${app.cookie.secure}")
    private boolean secure;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        TokenDto tokens = authService.refreshToken(refreshToken);

        ResponseCookie accessTokenCookie = createAccessTokenCookie(
                tokens.accessToken(),
                secure,
                jwtProperties.getAccessTokenExpiration());
        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(
                tokens.refreshToken(),
                secure,
                jwtProperties.getRefreshTokenExpiration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }


    @PostMapping("/check-id")
    public ResponseEntity<Void> checkIdDuplication(@RequestBody IdCheckRequest request) {
        authService.checkDuplicationId(request.loginId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> register(@RequestBody UserCreateRequest userCreateRequest) {
        authService.signUp(userCreateRequest);
        // 원래는 location을 적어줘야 한다?
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> signIn(@RequestBody LoginRequest request) {
        LoginDto loginDto = authService.signIn(request);

        ResponseCookie accessTokenCookie = createAccessTokenCookie(
                loginDto.accessToken(),
                secure,
                jwtProperties.getAccessTokenExpiration());

        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(
                loginDto.refreshToken(),
                secure,
                jwtProperties.getRefreshTokenExpiration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(loginDto.userDto());
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> logout() {

        ResponseCookie deleteAccessToken = createAccessTokenCookie("", secure, 0);
        ResponseCookie deleteRefreshToken = createRefreshTokenCookie( "", secure, 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccessToken.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefreshToken.toString())
                .build();
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal Long userId) {
        UserDto myProfile = authService.getMyProfile(userId);
        return ResponseEntity.ok(myProfile);
    }
}
