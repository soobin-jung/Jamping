package com.jam.ping.user.main.oauth;

import com.jam.ping.user.main.code.UserRole;
import com.jam.ping.user.main.domain.User;
import com.jam.ping.user.main.oauth.provider.OAuth2UserInfoExtractor;
import com.jam.ping.user.main.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;
    private final List<OAuth2UserInfoExtractor> extractors;

    /**
     * OAuth 제공자에게서 내려준 사용자 정보를 조회하고,
     * 우리 서비스 기준 사용자 정보로 정규화한 뒤 회원을 생성 또는 갱신합니다.
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oauth2UserInfo = findExtractor(registrationId)
                .extract(oauth2User.getAttributes());
        User user = upsertUser(oauth2UserInfo);

        return createPrincipal(user, oauth2UserInfo);
    }

    /**
     * registrationId를 처리할 수 있는 extractor를 선택합니다.
     */
    private OAuth2UserInfoExtractor findExtractor(String registrationId) {
        return extractors.stream()
                .filter(extractor -> extractor.supports(registrationId))
                .findFirst()
                .orElseThrow(() -> new OAuth2AuthenticationException("Unsupported provider: " + registrationId));
    }

    /**
     * 소셜 로그인 사용자 정보를 기준으로 회원을 조회하고,
     * 이미 존재하면 최신 프로필로 갱신하고 없으면 새로 저장합니다.
     */
    private User upsertUser(OAuth2UserInfo oauth2UserInfo) {
        return userRepository.findByProviderAndProviderUserId(
                        oauth2UserInfo.provider(),
                        oauth2UserInfo.providerUserId()
                )
                .map(existingUser -> {
                    existingUser.updateProfile(
                            oauth2UserInfo.email(),
                            oauth2UserInfo.nickname(),
                            oauth2UserInfo.profileImageUrl()
                    );
                    return existingUser;
                })
                .orElseGet(() -> userRepository.save(User.builder()
                        .provider(oauth2UserInfo.provider())
                        .providerUserId(oauth2UserInfo.providerUserId())
                        .email(oauth2UserInfo.email())
                        .nickname(oauth2UserInfo.nickname())
                        .profileImageUrl(oauth2UserInfo.profileImageUrl())
                        .role(UserRole.USER)
                        .build()));
    }

    /**
     * 세션에 저장할 인증 주체 객체를 생성합니다.
     */
    private OAuth2User createPrincipal(User user, OAuth2UserInfo oauth2UserInfo) {
        return new CustomOAuth2User(
                user.getId(),
                user.getProvider(),
                user.getProviderUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
                oauth2UserInfo.attributes()
        );
    }
}
