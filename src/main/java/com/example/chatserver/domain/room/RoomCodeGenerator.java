package com.example.chatserver.domain.room;

import java.security.SecureRandom;
import java.util.Base64;

public final class RoomCodeGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRoomCode() {
        byte[] buffer = new byte[9];                 // 9바이트 = 72비트
        RANDOM.nextBytes(buffer);
        return Base64.getUrlEncoder()                // URL 안전 문자(-, _)만 사용
                .withoutPadding()                    // '=' 제거
                .encodeToString(buffer);             // 12자 문자열
    }
}
