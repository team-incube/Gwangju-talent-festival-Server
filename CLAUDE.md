# Gwangju Talent Festival Server — Claude Control Point

## WHAT (기술 스택)

- **Runtime:** Java 21, Spring Boot 3.4.4
- **Database:** PostgreSQL (JPA/Hibernate) + Redis (캐싱, 토큰 저장)
- **Auth:** JWT (Access 15m / Refresh 14d) + NCP SMS OTP 인증
- **Real-time:** SSE (Server-Sent Events) — Vote, Judge, Seat 각각 EmitterManager
- **Monitoring:** Actuator + Prometheus + Grafana
- **Build:** `./gradlew clean build` / Test: `./gradlew test`
- **Base package:** `team.incube.gwangjutalentfestivalserver`

## WHY (아키텍처 원칙)

Clean Architecture + DDD. 각 도메인은 독립적이며, 의존성은 항상 안쪽(도메인)을 향한다.  
→ 세부 규칙: [`.claude/rules/architecture.md`](.claude/rules/architecture.md)

## HOW (운영)

```bash
# 빌드
./gradlew clean build -x test

# 테스트
./gradlew test

# 특정 테스트
./gradlew test --tests "team.incube.*DomainName*"

# 로컬 DB/Redis 실행
docker-compose up -d   # monitoring stack
```

## 도메인 목록

| 도메인 | 책임 |
|--------|------|
| `auth` | JWT 발급, SMS OTP, 회원가입/로그인 |
| `user` | 사용자·공연자 프로필 |
| `team` | 공연팀 CRUD, 랭킹 |
| `vote` | 실시간 투표 (SSE) |
| `judge` | 심사 점수 (SSE) |
| `seat` | 좌석 예약·금지 (SSE) |
| `excel` | 심사 결과 엑셀 내보내기 |

## 작업 라우팅 (Task Routing)

| 요청 유형 | 사용할 에이전트/스킬 |
|-----------|---------------------|
| 새 기능 구현 | `/planning` → `feature` 에이전트 |
| 버그 수정 | `bugfix` 에이전트 |
| 테스트 작성 | `test` 에이전트 |
| 코드 리뷰 | `review` 에이전트 |
| 리팩터링 | `refactor` 에이전트 |
| PR 작성 | `pr` 에이전트 |
| PR 피드백 처리 | `feedback` 에이전트 (Flywheel 허브) |
| UseCase 구현 | `/implement-usecase` 스킬 |
| 새 도메인 생성 | `/create-domain` 스킬 |
| 계획 수립 | `/planning` 스킬 |

## Agent Flywheel

```
요청
 ↓
[planning] 구현 계획 수립
 ↓
[feature] 코드 작성
 ↓
[test] 테스트 작성
 ↓
[review] 코드 리뷰 → 이슈 발견 시 [bugfix|refactor]
 ↓
[pr] PR 생성
 ↓ PR 피드백
[feedback] 피드백 라우팅 → 해당 에이전트로 재순환
 ↑_______________________________↑

각 사이클마다 .claude/flywheel/learnings.md에 학습 내용 축적
→ 다음 사이클에서 더 나은 코드 생성
```

## 코딩 규칙

- 코딩 표준: [`.claude/rules/coding-standards.md`](.claude/rules/coding-standards.md)
- Git 워크플로우: [`.claude/rules/git-workflow.md`](.claude/rules/git-workflow.md)
- 테스팅: [`.claude/rules/testing-standards.md`](.claude/rules/testing-standards.md)
- 플라이휠 학습: [`.claude/flywheel/learnings.md`](.claude/flywheel/learnings.md)