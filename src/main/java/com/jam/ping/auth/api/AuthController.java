package com.jam.ping.auth.api;

import com.jam.ping.auth.api.response.AuthMeResponse;
import com.jam.ping.global.response.ApiRes;
import com.jam.ping.user.main.oauth.CustomOAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 현재 세션의 로그인 사용자 정보를 공통 응답 형식으로 반환합니다.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiRes<AuthMeResponse>> me(@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.status(401)
                    .body(new ApiRes<AuthMeResponse>().responseMsg(401, "인증이 필요합니다."));
        }

        return ResponseEntity.ok(
                new ApiRes<AuthMeResponse>()
                        .successData(AuthMeResponse.from(oauth2User))
        );
    }
}
