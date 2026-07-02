# 컨트롤러 레이어 - DTO 사용 규칙

## 규칙

컨트롤러는 도메인 객체(Entity)를 직접 사용하지 않는다. 서비스가 반환하는 DTO만 사용한다.

## 구조

```
Controller → (DTO) ← Service → (Entity) ← Repository
```

- Service: Entity를 DTO로 변환하여 반환
- Controller: DTO를 Response로 변환하여 반환
- Entity는 Service 레이어 밖으로 나오지 않는다

## 예시

**잘못된 방식**
```java
// Controller에서 Entity 직접 사용
Gear gear = gearService.getGear(gearId);
return GearResponse.from(gear); // GearResponse.from(Gear)
```

**올바른 방식**
```java
// Service가 DTO 반환
GearDto gear = gearService.getGear(gearId);
return GearResponse.from(gear); // GearResponse.from(GearDto)
```

## 파일 위치

| 레이어 | 위치 |
|---|---|
| DTO | `api/{domain}/dto/{Domain}Dto.java` |
| Response | `api/{domain}/controller/response/{Domain}Response.java` |

## DTO 작성

```java
public record GearDto(Long id, String name, ...) {
    public static GearDto from(Gear gear) { ... }
}
```

## Response 작성

```java
public record GearResponse(Long id, String name, ...) {
    public static GearResponse from(GearDto dto) { ... }
}
```
