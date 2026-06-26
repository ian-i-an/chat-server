package com.example.chatserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //Auth
    DUPLICATE_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 후에 이용해주세요."),
    LOGIN_REQUIRED (HttpStatus.UNAUTHORIZED, "로그인 후에 이용해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "로그인이 만료되었습니다."),

    //User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    INVALID_ROOM_NAME(HttpStatus.BAD_REQUEST, "채팅방 이름이 유효하지 않습니다."),

    // Chat
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅입니다."),
    CHAT_NOT_IN_ROOM(HttpStatus.BAD_REQUEST, "해당 채팅방에 속하지 않는 채팅입니다."),
    EMPTY_CHAT_CONTENT(HttpStatus.BAD_REQUEST, "메시지 내용은 비어있을 수 없습니다."),

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
