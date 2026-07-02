# 도메인 생성 규칙 - 정적 팩토리 메서드

## 규칙

도메인 객체 생성은 `@Builder` 대신 정적 팩토리 메서드 `create()`를 사용한다.

## 이유

- `@Builder`는 필드를 선택적으로 채울 수 있어 필수 값이 누락되어도 컴파일 오류가 없다
- `create()`는 생성에 필요한 파라미터를 명확하게 강제한다

## 예시

**잘못된 방식**
```java
@Builder
public class Gear {
    private String name;
    private Category category;
    ...
}

// 호출 시
Gear gear = Gear.builder()
        .name(name)
        .build(); // category 누락되어도 컴파일 통과
```

**올바른 방식**
```java
public class Gear {
    private String name;
    private Category category;

    private Gear() {}

    public static Gear create(String name, Category category, ...) {
        Gear gear = new Gear();
        gear.name = name;
        gear.category = category;
        return gear;
    }
}

// 호출 시
Gear gear = Gear.create(name, category, ...); // 필수 파라미터 누락 시 컴파일 오류
```

## 규칙 요약

- 생성자는 `private` 또는 `protected`로 막는다
- `@Builder`는 Entity에 사용하지 않는다
- 메서드명은 `create`로 통일한다
- 생성에 필요한 모든 필수 값을 파라미터로 받는다
