package com.example.chatserver.global.exception;

import java.util.Map;

public record ErrorResponse(
        String errorCode,
        String message,
        Map<String,Object> details
) {
}
