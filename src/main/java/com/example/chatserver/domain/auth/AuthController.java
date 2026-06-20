package com.example.chatserver.domain.auth;

import com.example.chatserver.domain.auth.dto.LoginDto;
import com.example.chatserver.domain.auth.dto.TokenDto;
import com.example.chatserver.domain.auth.dto.request.IdCheckRequest;
import com.example.chatserver.domain.auth.dto.request.LoginRequest;
import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserCreateRequest;
import com.example.chatserver.global.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        TokenDto tokens = authService.refreshToken(refreshToken);

        ResponseCookie accessTokenCookie = createTokenCookie("access_token", tokens.accessToken(), jwtProperties.getAccessTokenExpiration());

        ResponseCookie refreshTokenCookie = createTokenCookie("refresh_token", tokens.refreshToken(), jwtProperties.getRefreshTokenExpiration());

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

        ResponseCookie accessTokenCookie = createTokenCookie("access_token",
                loginDto.accessToken(),
                jwtProperties.getAccessTokenExpiration());

        ResponseCookie refreshTokenCookie = createTokenCookie("refresh_token",
                loginDto.refreshToken(),
                jwtProperties.getRefreshTokenExpiration());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(loginDto.userDto());
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> logout() {

        ResponseCookie deleteAccessToken = createTokenCookie("access_token","",0);
        ResponseCookie deleteRefreshToken = createTokenCookie("refresh_token","",0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccessToken.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefreshToken.toString())
                .build();
    }

    public static ResponseCookie createTokenCookie(String tokenType, String token, long tokenExpiration) {
        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                //https일 때만 요청에 담아서 보내라! -> 지금은 개발이니까 false
                .secure(false)
                .path("/")
                .maxAge(tokenExpiration / 1000)
                .sameSite("Lax")
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal Long userId) {
        UserDto myProfile = authService.getMyProfile(userId);
        return ResponseEntity.ok(myProfile);
    }
}
