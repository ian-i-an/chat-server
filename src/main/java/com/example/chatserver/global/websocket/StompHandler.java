package com.example.chatserver.global.websocket;

import com.example.chatserver.global.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;


    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            Map<String, Object> attributes = accessor.getSessionAttributes();
            String token = (attributes != null) ? (String) attributes.get("token") : null;


            if (token != null) {
                try {
                    Claims claims = jwtProvider.parseClaims(token);
                    if ("access_token".equals(claims.get("type"))) {
                        String userId = claims.getSubject();
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

                        accessor.setUser(authentication);
                    }
                } catch (JwtException | IllegalArgumentException e) {
                    // 무효 토큰 → 익명 처리
                }
            }
        }
        return message;
    }
}