package com.example.chatserver.global.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, 
                         AuthenticationException authException) throws IOException {
        

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // 3. 프론트가 읽기 편한 예쁜 JSON 메시지 직접 작성해서 쏘기
        // Todo: 일관된 ErrorResponse 만들어서 반환
        response.getWriter().write("{\"status\": 401, \"message\": \"로그인이 필요하거나 토큰이 만료되었습니다.\"}");
    }
}