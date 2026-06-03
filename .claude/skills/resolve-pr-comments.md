---
name: resolve-pr-comments
description: PR 리뷰 코멘트를 해결하는 스킬. PR 번호를 제공하면 코멘트를 분석하고 수정한다.
---

# /resolve-pr-comments 스킬

PR 리뷰 코멘트를 체계적으로 해결한다.

## 사용법

```
/resolve-pr-comments {PR번호}

예시:
/resolve-pr-comments 60
```

## 해결 절차

### 1단계: 코멘트 수집
```bash
gh pr view {PR번호} --comments --json comments
gh pr diff {PR번호}
```

### 2단계: 코멘트 분류 및 우선순위 설정

| 우선순위 | 유형 | 처리 방법 |
|---------|------|----------|
| 1 | 버그/오류 지적 | 즉시 수정 |
| 2 | 아키텍처 위반 | 리팩터링 후 수정 |
| 3 | 코드 품질 | 수정 |
| 4 | 질문/설명 요청 | 답변 코멘트 작성 |
| 5 | nit/스타일 | 수정 (선택적) |

### 3단계: 각 코멘트 처리
각 코멘트에 대해:
1. 관련 코드 읽기
2. 지적 사항 이해
3. 수정 구현
4. 수정 내용 확인

### 4단계: 빌드 검증
```bash
./gradlew clean build -x test
```

### 5단계: Flywheel 학습 기록
`feedback` 에이전트를 통해 `.claude/flywheel/learnings.md`에 기록

### 6단계: 변경 사항 커밋
```bash
git add {수정된 파일들}
git commit -m "♻️ PR #{PR번호} 리뷰 코멘트 반영"
git push origin {현재-브랜치}
```

### 7단계: 코멘트에 답변
해결한 각 코멘트에 답변:
```bash
gh pr comment {PR번호} --body "..."
```

## 출력 형식

- 처리한 코멘트 수
- 각 코멘트별 해결 방법
- 남은 미해결 코멘트 (의도적으로 남긴 경우 이유 포함)
- 빌드 성공 확인