package com.jam.ping.api.user.main.domain;

import com.jam.ping.global.domain.CommonEntity;
import com.jam.ping.api.user.main.code.AuthProvider;
import com.jam.ping.api.user.main.code.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_provider_provider_user_id", columnNames = {"provider", "provider_user_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    @Column(nullable = false, length = 100)
    private String providerUserId;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    public static User create(AuthProvider provider, String providerUserId, String email, String nickname, String profileImageUrl, UserRole role) {
        User user = new User();
        user.provider = provider;
        user.providerUserId = providerUserId;
        user.email = email;
        user.nickname = nickname;
        user.profileImageUrl = profileImageUrl;
        user.role = role;
        return user;
    }

    public void updateProfile(String email, String nickname, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }
}
