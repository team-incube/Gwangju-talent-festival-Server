# 테스팅 표준

## 현황

현재 CI에서 테스트가 제외되어 있다 (`-x test`). 테스트 커버리지 확보가 필요하다.

## 테스트 구조

```
src/test/java/team/incube/gwangjutalentfestivalserver/
├── domain/
│   ├── {domain}/
│   │   ├── usecase/      ← UseCase 단위 테스트 (핵심)
│   │   └── entity/       ← Entity 비즈니스 로직 테스트
└── global/
    └── security/         ← JWT, 인증 테스트
```

## 테스트 레이어별 전략

### UseCase 테스트 (최우선)

```java
@ExtendWith(MockitoExtension.class)
class VoteParticipateUsecaseTest {

    @InjectMocks
    private VoteParticipateUsecase usecase;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserUtil userUtil;

    @Test
    @DisplayName("정상적으로 투표에 참여한다")
    void vote_participate_success() {
        // given
        given(voteRepository.findById(anyLong())).willReturn(Optional.of(vote));
        
        // when
        usecase.execute(request);
        
        // then
        verify(voteRepository).save(any(VoteUser.class));
    }

    @Test
    @DisplayName("이미 투표한 사용자는 중복 투표할 수 없다")
    void vote_participate_fail_duplicate() {
        // given - 이미 투표한 상태 세팅
        
        // when & then
        assertThatThrownBy(() -> usecase.execute(request))
            .isInstanceOf(HttpException.class);
    }
}
```

### Repository 테스트

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VoteRepositoryTest {
    // DB 통합 테스트 — TestContainers 사용 권장
}
```

### Controller 테스트

```java
@WebMvcTest(VoteController.class)
class VoteControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean VoteParticipateUsecase usecase;
    
    // HTTP 레이어만 테스트
}
```

## 규칙

- 테스트 메서드명: `@DisplayName`으로 한국어 명세 작성
- Given-When-Then 패턴 필수
- 성공 케이스와 실패 케이스 모두 작성
- Repository Mock은 `given(...).willReturn(...)` 패턴 사용 (BDDMockito)
- 하나의 테스트는 하나의 시나리오만 검증
- `verify()`로 중요한 사이드 이펙트(저장, 발행 등) 확인

## 실행

```bash
# 전체 테스트
./gradlew test

# 특정 도메인 테스트
./gradlew test --tests "team.incube.*.vote.*"

# 테스트 보고서
open build/reports/tests/test/index.html
```

## 테스트 데이터 픽스처

테스트용 객체 생성은 빌더 패턴 또는 정적 팩토리 메서드로 한 곳에서 관리한다.

```java
// TestFixture 클래스 예시
public class VoteFixture {
    public static Vote createActiveVote() { ... }
    public static Vote createFinishedVote() { ... }
}
```