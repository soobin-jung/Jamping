package com.jam.ping.user.oauth;

import com.jam.ping.user.domain.AuthProvider;
import java.util.Map;

public record OAuth2UserInfo(
        AuthProvider provider,
        String providerUserId,
        String email,
        String nickname,
        String profileImageUrl,
        Map<String, Object> attributes
) {
}
