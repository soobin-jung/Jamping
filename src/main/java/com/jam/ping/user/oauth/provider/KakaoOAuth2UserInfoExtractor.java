package com.jam.ping.user.oauth.provider;

import com.jam.ping.user.domain.AuthProvider;
import com.jam.ping.user.oauth.OAuth2UserInfo;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuth2UserInfoExtractor extends AbstractOAuth2UserInfoExtractor {

    /**
     * 카카오 로그인 registrationId를 처리합니다.
     */
    @Override
    public boolean supports(String registrationId) {
        return "kakao".equals(registrationId);
    }

    /**
     * 카카오 응답의 kakao_account/profile 구조를 파싱해 공통 사용자 정보로 변환합니다.
     * 현재는 닉네임만 동의받는 구조라 이메일은 null로 저장합니다.
     */
    @Override
    public OAuth2UserInfo extract(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = extractNestedMap(attributes, "kakao_account", "Invalid Kakao user response");
        Map<String, Object> profile = extractNestedMap(kakaoAccount, "profile", "Invalid Kakao profile response");

        return createUserInfo(
                AuthProvider.KAKAO,
                getRequiredValue(attributes, "id"),
                null,
                getNickname(profile, "kakao-user"),
                getOptionalValue(profile, "profile_image_url"),
                attributes
        );
    }
}
