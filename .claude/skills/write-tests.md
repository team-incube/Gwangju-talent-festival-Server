---
name: write-tests
description: 특정 도메인이나 UseCase에 대한 테스트를 작성하는 스킬.
---

# /write-tests 스킬

## 사용법

```
/write-tests {도메인} [{UseCase명}]

예시:
/write-tests vote                    ← vote 도메인 전체 UseCase 테스트
/write-tests vote VoteParticipate    ← 특정 UseCase만
/write-tests seat ReserveSeat
```

## 작성 절차

### 1단계: 대상 코드 읽기
```
src/main/java/.../domain/{도메인}/usecase/{UseCase}.java
src/main/java/.../domain/{도메인}/entity/
src/main/java/.../domain/{도메인}/repository/
```

### 2단계: 테스트 경우 목록 작성

각 UseCase에 대해:
- ✅ 정상 케이스 (성공)
- ❌ 실패 케이스들 (비즈니스 규칙 위반, 존재하지 않는 엔티티 등)
- 🔲 경계값 케이스

### 3단계: 테스트 작성

```java
// 파일 경로: src/test/java/team/incube/.../domain/{도메인}/usecase/{UseCase}Test.java
@ExtendWith(MockitoExtension.class)
@DisplayName("{UseCase} 테스트")
class {UseCase}Test {

    @InjectMocks
    private {UseCase} usecase;

    @Mock
    private {Domain}Repository repository;
    // 필요한 Mock들...

    @Test
    @DisplayName("정상적으로 {기능}한다")
    void {기능}_success() {
        // given
        given(repository.findById(anyLong()))
            .willReturn(Optional.of(createTest{Entity}()));
        
        // when
        usecase.execute(createTest{Request}());
        
        // then
        verify(repository).save(any());
    }

    @Test
    @DisplayName("{엔티티}가 존재하지 않으면 예외를 던진다")
    void {기능}_fail_notFound() {
        // given
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> usecase.execute(createTest{Request}()))
            .isInstanceOf(HttpException.class)
            .hasMessageContaining("찾을 수 없");
    }

    // 픽스처 메서드
    private {Entity} createTest{Entity}() {
        return {Entity}.builder()
            // 필드 설정
            .build();
    }
}
```

### 4단계: 실행 및 확인
```bash
./gradlew test --tests "team.incube.*.{도메인}.*"
```

### 5단계: 커버리지 확인
```bash
./gradlew test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```