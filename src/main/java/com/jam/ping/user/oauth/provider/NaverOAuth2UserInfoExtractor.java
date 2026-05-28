package com.jam.ping.user.oauth.provider;

import com.jam.ping.user.domain.AuthProvider;
import com.jam.ping.user.oauth.OAuth2UserInfo;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class NaverOAuth2UserInfoExtractor extends AbstractOAuth2UserInfoExtractor {

    /**
     * 네이버 로그인 registrationId를 처리합니다.
     */
    @Override
    public boolean supports(String registrationId) {
        return "naver".equals(registrationId);
    }

    /**
     * 네이버 응답의 response 블록을 파싱해 공통 사용자 정보로 변환합니다.
     */
    @Override
    public OAuth2UserInfo extract(Map<String, Object> attributes) {
        Map<String, Object> response = extractNestedMap(attributes, "response", "Invalid Naver user response");
        return createUserInfo(
                AuthProvider.NAVER,
                getRequiredValue(response, "id"),
                getOptionalValue(response, "email"),
                getNickname(response, "naver-user"),
                getOptionalValue(response, "profile_image"),
                response
        );
    }
}
