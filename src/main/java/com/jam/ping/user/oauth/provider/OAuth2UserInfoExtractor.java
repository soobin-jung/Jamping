package com.jam.ping.user.oauth.provider;

import com.jam.ping.user.oauth.OAuth2UserInfo;
import java.util.Map;

public interface OAuth2UserInfoExtractor {

    /**
     * 현재 extractor가 특정 OAuth 제공자를 처리할 수 있는지 확인합니다.
     */
    boolean supports(String registrationId);

    /**
     * 제공자별 원본 속성을 우리 서비스의 공통 사용자 정보 형태로 변환합니다.
     */
    OAuth2UserInfo extract(Map<String, Object> attributes);
}
