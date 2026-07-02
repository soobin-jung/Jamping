# 도메인 Entity 규칙

## 1. CommonEntity 상속

모든 Entity는 `CommonEntity`를 상속받는다.

`CommonEntity`는 아래 필드를 자동으로 관리한다.

| 필드 | 설명 |
|---|---|
| `createdAt` | 생성 시각 (자동) |
| `updatedAt` | 수정 시각 (자동) |
| `createdById` | 생성자 유저 ID (자동) |
| `updatedById` | 수정자 유저 ID (자동) |

```java
public class Gear extends CommonEntity {
    ...
}
```

## 2. @Table 생략

`@Table`은 명시하지 않는다. 전역 네이밍 전략이 클래스명을 snake_case 테이블명으로 자동 변환한다.

```java
// 잘못된 방식
@Entity
@Table(name = "gear")
public class Gear extends CommonEntity { ... }

// 올바른 방식
@Entity
public class Gear extends CommonEntity { ... }
```
