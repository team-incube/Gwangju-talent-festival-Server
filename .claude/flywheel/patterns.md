# Flywheel Patterns

> 반복적으로 발견되는 패턴과 안티패턴을 기록한다.
> `feature` 에이전트와 `/planning` 스킬에서 참조하여 사전 방지한다.

## 좋은 패턴 (따라야 할 것)

### SSE 연결 관리
- EmitterManager에서 `onCompletion`/`onTimeout`/`onError` 콜백으로 반드시 cleanup
- 타임아웃 설정 필수 (무한 연결 방지)

### 트랜잭션 경계
- `@Transactional`은 UseCase 메서드에만
- 읽기 전용 쿼리는 `@Transactional(readOnly = true)` 명시

### Redis 사용
- Key 네이밍: `{도메인}:{타입}:{식별자}` 패턴 유지
- TTL 항상 명시적 설정

---

## 안티패턴 (피해야 할 것)

*(PR 피드백을 통해 발견된 안티패턴이 기록됩니다)*

---

<!-- 
## {패턴명}
**유형:** 좋은 패턴 | 안티패턴
**발견:** {발견된 컨텍스트}
**설명:** {패턴 설명}
**예시:**
```java
// 코드 예시
```
-->