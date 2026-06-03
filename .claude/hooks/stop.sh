#!/usr/bin/env bash
# stop.sh — Claude 작업 종료 시 실행되는 Flywheel 훅
# 세션 로그를 요약하고 다음 사이클을 위한 컨텍스트를 준비

set -euo pipefail

LOG_DIR=".claude/flywheel"
SESSION_LOG="$LOG_DIR/session.log"
SUMMARY_LOG="$LOG_DIR/sessions.md"

# 세션 로그가 없으면 종료
if [[ ! -f "$SESSION_LOG" ]]; then
  exit 0
fi

TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
CHANGED_FILES=$(sort -u "$SESSION_LOG" | grep -oE 'src/[^ ]+\.java' | sort -u | head -20 || true)
FILE_COUNT=$(printf '%s' "$CHANGED_FILES" | grep -c '.java' || echo "0")

if [[ "$FILE_COUNT" -eq 0 ]]; then
  rm -f "$SESSION_LOG"
  exit 0
fi

# sessions.md에 세션 요약 추가
{
  printf '\n## 세션: %s\n' "$TIMESTAMP"
  printf '**수정된 파일 (%s개):**\n' "$FILE_COUNT"
  printf '%s\n' "$CHANGED_FILES" | while read -r f; do
    printf '- %s\n' "$f"
  done
  printf '\n'
} >> "$SUMMARY_LOG"

# 세션 로그 초기화
rm -f "$SESSION_LOG"

printf '[Flywheel] 세션 요약이 %s에 기록되었습니다.\n' "$SUMMARY_LOG"

exit 0