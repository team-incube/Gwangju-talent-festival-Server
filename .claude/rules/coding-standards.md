# 코딩 표준

## Java / Spring Boot 규칙

### 일반

- Java 21 기능 활용 (record, sealed class, pattern matching, text block)
- Lombok: `@RequiredArgsConstructor`, `@Getter`, `@Builder` 적극 사용
- `@Setter` 사용 금지 — 불변 객체 선호
- `var` 사용 가능하나 타입이 명확하지 않으면 명시적 타입 선호
- 메서드 길이: 30줄 초과 시 분리를 고려한다

### 예외 처리

- `HttpException`을 상속하여 도메인별 예외를 만든다
- Controller에서 예외를 catch하지 않는다 — `HttpExceptionHandler`가 처리
- 예외 메시지는 한국어로 작성한다 (사용자 대면 메시지)

```java
// 올바른 예외 사용
throw new HttpException(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다.");
```

### 트랜잭션

- `@Transactional`은 UseCase에만 붙인다
- 읽기 전용: `@Transactional(readOnly = true)`
- Repository 메서드에는 트랜잭션 어노테이션을 붙이지 않는다

### Redis

- Key 패턴: `{도메인}:{타입}:{식별자}` (예: `auth:refresh-token:userId`)
- TTL은 항상 명시적으로 설정한다
- `RedisTemplate` 또는 `@RedisHash` + Repository 패턴 사용

### JWT / 인증

- 현재 사용자는 `UserUtil`을 통해 SecurityContext에서 가져온다
- Controller에서 직접 SecurityContext를 접근하지 않는다
- `@AuthenticationPrincipal`이나 `UserUtil.getCurrentUser()` 사용

### SSE

- SSE 응답 타입: `text/event-stream`
- 연결 timeout: `SseEmitter`에 명시적 timeout 설정
- 연결 해제 시 EmitterManager에서 제거하는 cleanup 로직 필수

### DTO

- Request DTO에는 `@Valid` 어노테이션과 Bean Validation 사용
- Response DTO는 record 사용 권장 (불변)
- `@NotNull`, `@NotBlank`, `@Size`, `@Min`, `@Max` 등 활용

### Excel (Apache POI)

- Builder 패턴으로 엑셀 생성 로직 분리 (`{Domain}ExcelBuilder`)
- 스트리밍 방식(`SXSSFWorkbook`) 사용 (대용량 데이터 대비)

## 패키지 임포트 순서

1. Java 표준 라이브러리
2. 외부 라이브러리 (Spring, Lombok, etc.)
3. 내부 패키지 (`team.incube.*`)

## 주석 규칙

- 비즈니스 로직이 자명하지 않을 때만 주석 추가
- Javadoc은 public API에만 (Controller 메서드, UseCase execute())
- TODO 주석은 GitHub Issue 번호와 함께: `// TODO #123: 설명`