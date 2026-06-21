package com.jam.ping.user.main.oauth;

import com.jam.ping.user.main.code.AuthProvider;
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
