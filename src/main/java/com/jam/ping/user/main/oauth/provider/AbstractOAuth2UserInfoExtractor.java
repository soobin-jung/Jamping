package com.jam.ping.user.main.oauth.provider;

import com.jam.ping.user.main.code.AuthProvider;
import com.jam.ping.user.main.oauth.OAuth2UserInfo;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

abstract class AbstractOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    /**
     * 중첩된 사용자 정보 맵을 추출합니다.
     * 제공자 응답 구조가 예상과 다르면 인증 예외를 발생시킵니다.
     */
    protected Map<String, Object> extractNestedMap(Map<String, Object> attributes, String key, String errorMessage) {
        Object value = attributes.get(key);
        if (value instanceof Map<?, ?> nestedMap) {
            @SuppressWarnings("unchecked")
            Map<String, Object> casted = (Map<String, Object>) nestedMap;
            return casted;
        }
        throw new OAuth2AuthenticationException(errorMessage);
    }

    /**
     * 반드시 필요한 속성 값을 문자열로 반환합니다.
     */
    protected String getRequiredValue(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        if (value == null || value.toString().isBlank()) {
            throw new OAuth2AuthenticationException("Missing required attribute: " + key);
        }
        return value.toString();
    }

    /**
     * 선택 속성 값을 문자열로 반환합니다.
     * 값이 없으면 null을 반환합니다.
     */
    protected String getOptionalValue(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        if (value == null || value.toString().isBlank()) {
            return null;
        }
        return value.toString();
    }

    /**
     * 제공자별 응답에서 닉네임을 우선 조회하고,
     * 없으면 name, 그것도 없으면 기본값을 사용합니다.
     */
    protected String getNickname(Map<String, Object> attributes, String fallback) {
        Object nickname = attributes.get("nickname");
        if (nickname != null && !nickname.toString().isBlank()) {
            return nickname.toString();
        }

        Object name = attributes.get("name");
        if (name != null && !name.toString().isBlank()) {
            return name.toString();
        }

        return fallback;
    }

    /**
     * 공통 OAuth2UserInfo 객체를 생성하면서 provider 정보도 함께 속성에 주입합니다.
     */
    protected OAuth2UserInfo createUserInfo(
            AuthProvider provider,
            String providerUserId,
            String email,
            String nickname,
            String profileImageUrl,
            Map<String, Object> attributes
    ) {
        Map<String, Object> normalized = new LinkedHashMap<>(attributes);
        normalized.put("provider", provider.name());

        return new OAuth2UserInfo(
                provider,
                providerUserId,
                email,
                nickname,
                profileImageUrl,
                normalized
        );
    }
}
