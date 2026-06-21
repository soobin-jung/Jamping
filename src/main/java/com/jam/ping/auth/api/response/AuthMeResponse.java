package com.jam.ping.auth.api.response;

import com.jam.ping.user.main.oauth.CustomOAuth2User;

public record AuthMeResponse(
        Long userId,
        String provider,
        String providerUserId,
        String nickname,
        String email,
        String role
) {

    /**
     * 인증 사용자 정보를 /api/auth/me 응답 형식으로 변환합니다.
     */
    public static AuthMeResponse from(CustomOAuth2User oauth2User) {
        return new AuthMeResponse(
                oauth2User.getUserId(),
                oauth2User.getProvider().name(),
                oauth2User.getProviderUserId(),
                oauth2User.getNickname(),
                oauth2User.getEmail(),
                oauth2User.getRole().name()
        );
    }
}
