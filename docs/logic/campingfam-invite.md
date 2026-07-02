# 캠핑팸 유저 초대 기능

## 흐름

### 초대 발송

```
POST /camping-fams/{campingFamId}/invitations
```

1. 초대자가 해당 캠핑팸 멤버인지 확인 (아니면 403)
2. 이메일 목록 순회
   - 해당 캠핑팸에 동일 이메일로 `PENDING` 초대가 이미 있으면 skip
   - UUID 토큰 생성, 만료일 = 현재 + 7일
   - `CampingFamInvitation.create()` 저장
   - 해당 이메일로 가입된 유저가 있으면 → 앱 내 알림 생성
   - 가입되지 않은 이메일이면 → 초대 이메일 발송

### 수락

```
POST /camping-fams/invitations/{token}/accept
```

1. 토큰으로 초대 조회 (없으면 404)
2. 만료 여부 확인 (만료 시 상태를 `EXPIRED`로 변경 후 400)
3. 상태가 `PENDING`이 아니면 400
4. 이미 해당 캠핑팸 멤버인지 확인 (이미 멤버면 409)
5. `CampingFamMember.create(campingFam, user, GUEST)` 저장
6. 초대 상태 → `ACCEPTED`

### 거절

```
POST /camping-fams/invitations/{token}/reject
```

1. 토큰으로 초대 조회
2. 상태가 `PENDING`이 아니면 400
3. 초대 상태 → `REJECTED`

## 초대 상태

| 상태 | 설명 |
|---|---|
| `PENDING` | 초대 발송됨, 응답 대기 중 |
| `ACCEPTED` | 수락됨 |
| `REJECTED` | 거절됨 |
| `EXPIRED` | 만료됨 (7일 경과) |

## 관련 파일

| 역할 | 파일 |
|---|---|
| 초대 로직 | `CampingFamInvitationService` |
| 도메인 | `CampingFamInvitation`, `CampingFamMember` |
| 이메일 발송 | `EmailService` |
| 알림 | `NotificationService` |
