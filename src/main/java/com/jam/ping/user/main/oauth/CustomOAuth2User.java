package com.jam.ping.user.main.oauth;

import com.jam.ping.user.main.code.AuthProvider;
import com.jam.ping.user.main.code.UserRole;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final Long userId;
    private final AuthProvider provider;
    private final String providerUserId;
    private final String email;
    private final String nickname;
    private final UserRole role;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(
            Long userId,
            AuthProvider provider,
            String providerUserId,
            String email,
            String nickname,
            UserRole role,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes
    ) {
        this.userId = userId;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    /**
     * 현재 로그인한 사용자 엔티티의 ID를 반환합니다.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 로그인한 사용자의 소셜 로그인 제공자를 반환합니다.
     */
    public AuthProvider getProvider() {
        return provider;
    }

    /**
     * 소셜 제공자 내부에서 사용자를 식별하는 고유 ID를 반환합니다.
     */
    public String getProviderUserId() {
        return providerUserId;
    }

    /**
     * 제공자가 내려준 이메일을 반환합니다.
     * 제공자가 이메일을 주지 않는 경우 null일 수 있습니다.
     */
    public String getEmail() {
        return email;
    }

    /**
     * 화면 표시용 닉네임 또는 이름을 반환합니다.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 현재 로그인한 사용자의 권한 역할을 반환합니다.
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * OAuth 제공자가 내려준 원본 속성 맵을 반환합니다.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 현재 인증 주체가 가진 권한 목록을 반환합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Spring Security가 인증 주체를 식별할 때 사용할 이름을 반환합니다.
     */
    @Override
    public String getName() {
        return providerUserId;
    }
}
