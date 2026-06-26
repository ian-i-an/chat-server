package com.example.chatserver.domain.user;

import com.example.chatserver.domain.user.dto.UserDto;
import com.example.chatserver.domain.user.dto.request.UserUpdateRequest;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    @Value("${app.cookie.secure}")
    private  boolean secure;

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Long userId) {
        userService.deleteUserById(userId);

        ResponseCookie deleteAccessToken = createAccessTokenCookie("",secure,0);
        ResponseCookie deleteRefreshToken = createRefreshTokenCookie("",secure,0);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, deleteAccessToken.toString())
                .header(HttpHeaders.SET_COOKIE, deleteRefreshToken.toString())
                .build();
    }

    @PatchMapping
    public ResponseEntity<UserDto> updateUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserUpdateRequest userUpdateRequest) {
        UserDto userDto = userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(userDto);
    }
}
