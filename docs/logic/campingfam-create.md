# 캠핑팸 생성 기능

## 흐름

```
POST /camping-fams
```

1. 요청에서 `name`, `campSiteId`, `reservationSites` 수신
2. `campSiteId`로 캠핑장 조회 (없으면 404)
3. 현재 로그인 유저 조회 (`AuthUtils.getCurrentUserId()`)
4. `CampingFam.create()` 저장
5. 생성자를 `HOST` 역할로 `CampingFamMember` 저장
6. `CampingFamDto` 반환

## 핵심 규칙

- 생성자는 자동으로 `HOST`가 된다
- 일정(캠핑 날짜)은 생성 시 설정하지 않는다. 별도로 일정 확정 API(`finalizeSchedule`)를 통해 설정한다

## 관련 파일

| 역할 | 파일 |
|---|---|
| 생성 로직 | `CampingFamService.createCampingFam()` |
| 도메인 | `CampingFam`, `CampingFamMember` |
