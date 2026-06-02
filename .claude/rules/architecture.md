# 아키텍처 규칙

## Clean Architecture + DDD 원칙

### 레이어 구조 (도메인별)

```
domain/{도메인명}/
├── controller/          # Presentation Layer — HTTP 요청/응답
├── usecase/             # Application Layer — 비즈니스 유스케이스 (1 클래스 = 1 기능)
├── entity/              # Domain Layer — 핵심 비즈니스 객체
├── repository/          # Domain Layer — 저장소 인터페이스 (interface)
├── dto/                 # Data Transfer Objects
│   ├── request/
│   └── response/
├── event/               # 도메인 이벤트 + 핸들러
└── enums/               # 도메인 열거형
```

### 의존성 방향

```
Controller → UseCase → Repository (interface)
                ↓
           Entity (순수 도메인 객체)
```

- Controller는 UseCase만 의존한다. Repository를 직접 사용하지 않는다.
- UseCase는 Repository 인터페이스에만 의존한다 (구현체 X).
- Entity에는 비즈니스 로직을 넣을 수 있다.
- 도메인 간 직접 의존 금지 — 이벤트(ApplicationEvent)로 통신한다.

### 명명 규칙

| 레이어 | 패턴 | 예시 |
|--------|------|------|
| Controller | `{Domain}Controller` | `VoteController` |
| UseCase | `{Action}{Domain}Usecase` | `VoteParticipateUsecase` |
| Entity | `{Domain}` (단수) | `Vote`, `SeatReservation` |
| Repository | `{Entity}Repository` | `VoteRepository` |
| Request DTO | `{Action}{Domain}Request` | `VoteParticipateRequest` |
| Response DTO | `Get{Domain}Response` | `VoteResultResponse` |
| Event | `{Domain}{Action}Event` | `VoteChangeEvent` |
| Handler | `{Event}Handler` | `VoteChangeEventHandler` |

### UseCase 규칙

- 한 UseCase는 하나의 public 메서드만 가진다 (`execute()`).
- `@Transactional`은 UseCase 레벨에서 선언한다.
- `@Service` 어노테이션을 사용한다.
- 생성자 주입 + Lombok `@RequiredArgsConstructor` 사용.

### SSE (Server-Sent Events) 패턴

- 각 실시간 도메인(Vote, Judge, Seat)은 전용 EmitterManager를 `global/sse/`에 둔다.
- SSE 연결 UseCase: `ConnectSse{Domain}EventUsecase`
- 이벤트 발생 시 해당 EmitterManager를 통해 브로드캐스트.

### 이벤트 기반 도메인 간 통신

```java
// 이벤트 발행 (UseCase 내에서)
applicationEventPublisher.publishEvent(new VoteChangeEvent(this, voteId));

// 이벤트 핸들러 (같은 도메인 또는 global에 위치)
@EventListener
@Async
public void handle(VoteChangeEvent event) { ... }
```