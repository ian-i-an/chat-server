package com.example.chatserver.domain.user;

import com.example.chatserver.domain.base.SoftDeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends SoftDeletableEntity {

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String passwordHash;

    public static User create(String nickname, String passwordHash) {
        User user = new User();

        requireNonNull(nickname);
        requireNonNull(passwordHash);

        user.nickname = nickname;
        user.passwordHash = passwordHash;
        return user;
    }



    public void changePasswordHash(String passwordHash) {
        this.passwordHash = requireNonNull(passwordHash);
    }
}
