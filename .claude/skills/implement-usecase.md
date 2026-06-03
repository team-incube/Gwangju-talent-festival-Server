---
name: implement-usecase
description: 새로운 UseCase를 구현하는 절차적 스킬. 도메인명과 기능명을 제공하면 전체 스캐폴딩을 생성한다.
---

# /implement-usecase 스킬

새 UseCase를 구현할 때 이 스킬을 사용한다.

## 사용법

```
/implement-usecase {도메인} {액션} [{HTTP 메서드} {경로}]

예시:
/implement-usecase vote cancel DELETE /api/v1/votes/{voteId}
/implement-usecase team approve PATCH /api/v1/teams/{teamId}/approve
```

## 구현 체크리스트

### 1단계: 기존 패턴 파악
- [ ] `domain/{도메인}/usecase/` 내 기존 UseCase 파일 하나 읽기
- [ ] `domain/{도메인}/controller/{도메인}Controller.java` 읽기
- [ ] 기존 패턴과 동일한 스타일로 구현

### 2단계: DTO 작성 (필요시)
```
src/main/java/team/incube/.../domain/{도메인}/dto/
  request/{Action}{Domain}Request.java   ← @Valid, @NotNull 등
  response/Get{Domain}Response.java      ← record 권장
```

### 3단계: UseCase 작성
```java
// 파일: domain/{도메인}/usecase/{Action}{Domain}Usecase.java
@Service
@RequiredArgsConstructor
public class {Action}{Domain}Usecase {
    private final {Domain}Repository {domain}Repository;
    // 필요한 의존성...
    
    @Transactional
    public {반환타입} execute({파라미터}) {
        // 비즈니스 로직
    }
}
```

### 4단계: Controller에 엔드포인트 추가
```java
// domain/{도메인}/controller/{Domain}Controller.java에 메서드 추가
@{HttpMethod}Mapping("{경로}")
public ResponseEntity<{Response}> {메서드명}(...) {
    return ResponseEntity.ok(usecase.execute(...));
}
```

### 5단계: Security 설정 확인
- [ ] `global/security/SecurityConfig.java`에 경로 권한 설정 추가
- [ ] 인증 필요 여부 확인

### 6단계: 검증
- [ ] `./gradlew compileJava` — 컴파일 오류 없음
- [ ] `./gradlew clean build -x test` — 빌드 성공

### 7단계: 테스트
- [ ] `test` 에이전트 호출하여 UseCase 테스트 작성 권장