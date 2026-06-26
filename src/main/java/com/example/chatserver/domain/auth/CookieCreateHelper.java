package com.example.chatserver.domain.auth;
import org.springframework.http.ResponseCookie;

public final class CookieCreateHelper {

    public CookieCreateHelper( ) {
        throw new AssertionError("이 클래스는 인스턴스화할 수 없습니다.");
    }

    public static ResponseCookie createTokenCookie(String tokenType, String token, boolean secure, long tokenExpiration) {
//        .path("/api/auth/refresh")   // 다른 요청엔 아예 안 실림 → 노출면 축소
//                .sameSite("Strict")
        // todo: refresh토큰에는 더 제한 걸기
        return ResponseCookie.from(tokenType, token)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(tokenExpiration / 1000)
                .sameSite("Lax")
                .build();
    }
}
