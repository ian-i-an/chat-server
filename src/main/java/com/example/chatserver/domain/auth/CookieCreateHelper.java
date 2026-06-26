package com.example.chatserver.domain.auth;
import org.springframework.http.ResponseCookie;

public final class CookieCreateHelper {

    public CookieCreateHelper( ) {
        throw new AssertionError("이 클래스는 인스턴스화할 수 없습니다.");
    }

    public static ResponseCookie createAccessTokenCookie(String token, boolean secure, long tokenExpiration) {

        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(tokenExpiration / 1000)
                .sameSite("Strict")
                .build();
    }


    public static ResponseCookie createRefreshTokenCookie(String token, boolean secure, long tokenExpiration) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(secure)
                .path("/api/auth/refresh")
                .maxAge(tokenExpiration / 1000)
                .sameSite("Strict")
                .build();
    }
}
