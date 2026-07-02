# Exception Rule

## 커스텀 예외 클래스를 사용한다

`ResponseStatusException`을 직접 던지지 않는다.
`global/exception/` 하위의 커스텀 예외 클래스를 사용한다.

## 예외 계층 구조

```
BusinessException (RuntimeException)
├── NotFoundException       → 404
├── BadRequestException     → 400
├── ConflictException       → 409
├── ForbiddenException      → 403
└── UnauthorizedException   → 401
```

## 사용법

```java
// ❌
throw new ResponseStatusException(HttpStatus.NOT_FOUND, "장비를 찾을 수 없습니다.");

// ✓
throw new NotFoundException("장비를 찾을 수 없습니다.");
```

## 처리

`GlobalExceptionHandler`에서 `BusinessException`을 일괄 처리한다.
새로운 HTTP 상태가 필요한 경우 `BusinessException`을 상속한 클래스를 추가한다.
