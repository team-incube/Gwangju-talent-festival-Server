# Git 워크플로우

## 브랜치 전략

```
main          ← 프로덕션 (직접 push 금지)
develop       ← 통합 브랜치 (직접 push 금지)
  ↑
feature/{issue-number}-{description}    ← 기능 개발
fix/{issue-number}-{description}        ← 버그 수정
refactor/{issue-number}-{description}   ← 리팩터링
chore/{issue-number}-{description}      ← 설정, 의존성
```

## 커밋 메시지 컨벤션

이 프로젝트는 Gitmoji + 한국어 메시지를 사용한다.

```
{emoji} {한국어 설명}

예시:
✨ 투표 참여 UseCase 추가
🐛 좌석 예약 중복 체크 오류 수정
♻️ VoteRandomExtractUsecase 추출 로직 분리
🔥 사용하지 않는 VoteRandomExtractUsecase 제거
💄 응답 DTO 필드명 camelCase로 통일
```

| Emoji | 의미 |
|-------|------|
| ✨ | 새 기능 |
| 🐛 | 버그 수정 |
| ♻️ | 리팩터링 |
| 🔥 | 코드/파일 삭제 |
| 💄 | UI/스타일 (DTO, 응답 형식) |
| 🎨 | 코드 구조 개선 |
| ⚡️ | 성능 개선 |
| 🔒 | 보안 수정 |
| 📝 | 문서 |
| 🧪 | 테스트 |
| 🔧 | 설정 변경 |
| ⬆️ | 의존성 업그레이드 |

## PR 규칙

- PR 제목: `[{타입}] {설명}` (예: `[Feature] 팀 랭킹 조회 API 추가`)
- PR 본문에는 변경사항, 테스트 방법, 관련 Issue 번호 포함
- 최소 1명의 리뷰어 승인 후 Squash Merge
- PR 생성 전 `./gradlew clean build` 성공 확인

## 금지 사항

- `main`, `develop`에 직접 push 금지
- `--force` push 금지 (rebase 후 force push 필요 시 팀 협의)
- `git reset --hard`로 공유 커밋 변경 금지
- 커밋에 `.env`, 시크릿 값, 개인정보 포함 금지

## Issue 연결

```bash
# PR에서 Issue 자동 닫기
git commit -m "✨ 팀 랭킹 API 구현 (Closes #56)"
```