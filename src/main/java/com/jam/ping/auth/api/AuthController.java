package com.jam.ping.auth.api;

import com.jam.ping.user.oauth.CustomOAuth2User;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 현재 세션에 로그인된 사용자 정보를 반환합니다.
     * 비로그인 상태라면 401 응답을 반환합니다.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.status(401).build();
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("provider", oauth2User.getProvider().name());
        response.put("providerUserId", oauth2User.getProviderUserId());
        response.put("nickname", oauth2User.getNickname());
        response.put("email", oauth2User.getEmail());

        return ResponseEntity.ok(response);
    }
}
