#!/usr/bin/env bash
# post-tool-use.sh — 파일 수정 후 실행되는 훅
# 파일이 수정되면 로그를 기록하고 필요시 코드 품질 검사를 트리거

set -euo pipefail

INPUT=$(cat)

TOOL_NAME=$(printf '%s' "$INPUT" | python3 -c "
import sys, json
try:
    d = json.load(sys.stdin)
    print(d.get('tool_name', ''))
except:
    print('')
" 2>/dev/null || true)

FILE_PATH=$(printf '%s' "$INPUT" | python3 -c "
import sys, json
try:
    d = json.load(sys.stdin)
    inp = d.get('tool_input', {})
    print(inp.get('file_path', inp.get('path', '')))
except:
    print('')
" 2>/dev/null || true)

# Java 파일이 수정된 경우만 처리
if [[ "$FILE_PATH" != *.java ]]; then
  exit 0
fi

LOG_DIR=".claude/flywheel"
LOG_FILE="$LOG_DIR/session.log"

mkdir -p "$LOG_DIR"

TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
printf '[%s] %s → %s\n' "$TIMESTAMP" "$TOOL_NAME" "$FILE_PATH" >> "$LOG_FILE"

exit 0