package com.example.chatserver.domain.user;

import com.example.chatserver.domain.base.AuditingEntity;
import com.example.chatserver.domain.base.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingEntity {

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false,  unique = true)
    private String loginId;

    @Column(nullable = false)
    private String passwordHash;

    public static User create(String nickname, String loginId, String passwordHash) {
        User user = new User();
        user.nickname = requireNonNull(nickname);;
        user.loginId =  requireNonNull(loginId);;
        user.passwordHash =  requireNonNull(passwordHash);;
        return user;
    }

    public void changePasswordHash(String passwordHash) {
        this.passwordHash = requireNonNull(passwordHash);
    }
    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }
}
