# 로그인 기능

OAuth2 소셜 로그인만 지원한다. (자체 로그인 없음)

## 흐름

```
클라이언트 → OAuth2 제공자 인증 → 콜백 → CustomOAuth2UserService → 세션 저장
```

1. 클라이언트가 소셜 로그인 버튼 클릭 → OAuth2 제공자(Google 등)로 리디렉션
2. 인증 완료 후 콜백 URL로 리디렉션
3. `CustomOAuth2UserService.loadUser()` 실행
4. `OAuth2UserInfoExtractor`로 제공자별 응답에서 사용자 정보 정규화
5. `upsertUser()` 실행
   - 기존 유저(`provider` + `providerUserId` 기준 조회): 이메일·닉네임·프로필 이미지 갱신
   - 신규 유저: `User.create()`로 저장, 기본 역할 `USER`
6. `CustomOAuth2User` 생성 후 세션에 저장

## 관련 파일

| 역할 | 파일 |
|---|---|
| OAuth2 유저 처리 | `CustomOAuth2UserService` |
| 제공자별 정보 추출 | `OAuth2UserInfoExtractor` 구현체들 |
| 인증 주체 | `CustomOAuth2User` |
| 유저 도메인 | `User` |
