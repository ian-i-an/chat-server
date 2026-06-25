package com.example.chatserver.domain.auth;
import org.springframework.http.ResponseCookie;

public final class CookieCreateHelper {

    public CookieCreateHelper( ) {
        throw new AssertionError("이 클래스는 인스턴스화할 수 없습니다.");
    }

    public static ResponseCookie createTokenCookie(String tokenType, String token, boolean secure, long tokenExpiration) {
        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                //https일 때만 요청에 담아서 보내라! -> 지금은 개발이니까 false
                .secure(secure)
                .path("/")
                .maxAge(tokenExpiration / 1000)
                .sameSite("Lax")
                .build();
    }
}
