package com.jam.ping.user.oauth.provider;

import com.jam.ping.user.code.AuthProvider;
import com.jam.ping.user.oauth.OAuth2UserInfo;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuth2UserInfoExtractor extends AbstractOAuth2UserInfoExtractor {

    /**
     * 구글 로그인 registrationId를 처리합니다.
     */
    @Override
    public boolean supports(String registrationId) {
        return "google".equals(registrationId);
    }

    /**
     * 구글 OIDC 사용자 정보를 파싱해 공통 사용자 정보로 변환합니다.
     */
    @Override
    public OAuth2UserInfo extract(Map<String, Object> attributes) {
        return createUserInfo(
                AuthProvider.GOOGLE,
                getRequiredValue(attributes, "sub"),
                getOptionalValue(attributes, "email"),
                getNickname(attributes, "google-user"),
                getOptionalValue(attributes, "picture"),
                attributes
        );
    }
}
