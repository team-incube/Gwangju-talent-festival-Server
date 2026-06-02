#!/usr/bin/env bash
# pre-tool-use.sh — Claude Code 사전 안전 검사 훅
# stdin으로 JSON tool 정보를 받아 위험한 명령을 차단한다

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

COMMAND=$(printf '%s' "$INPUT" | python3 -c "
import sys, json
try:
    d = json.load(sys.stdin)
    inp = d.get('tool_input', {})
    print(inp.get('command', ''))
except:
    print('')
" 2>/dev/null || true)

# Bash 도구만 검사
if [[ "$TOOL_NAME" != "Bash" ]]; then
  exit 0
fi

# ── 1. force push 차단 ─────────────────────────────────────────
if printf '%s' "$COMMAND" | grep -qE 'git push.*(--force|-f)'; then
  printf '[BLOCKED] force push는 허용되지 않습니다. 팀원과 협의하세요.\n' >&2
  exit 2
fi

# ── 2. 보호 브랜치 직접 push 차단 ────────────────────────────
if printf '%s' "$COMMAND" | grep -qE 'git push[^|&;]*(origin)?\s+(main|develop|master)\b'; then
  printf '[BLOCKED] main/develop 브랜치에 직접 push할 수 없습니다. PR을 사용하세요.\n' >&2
  exit 2
fi

# ── 3. rm -rf 차단 ────────────────────────────────────────────
if printf '%s' "$COMMAND" | grep -qE 'rm\s+-[a-z]*r[a-z]*f|rm\s+-[a-z]*f[a-z]*r'; then
  printf '[BLOCKED] rm -rf는 허용되지 않습니다.\n' >&2
  exit 2
fi

# ── 4. git reset --hard 차단 ──────────────────────────────────
if printf '%s' "$COMMAND" | grep -qE 'git reset --hard'; then
  printf '[BLOCKED] git reset --hard는 허용되지 않습니다. 변경사항이 유실될 수 있습니다.\n' >&2
  exit 2
fi

# ── 5. 복합 명령 체인 경고 (정보성) ──────────────────────────
# 단순한 파이프(|)와 &&/|| 구분: git, gradle 등 일반 파이프는 허용
if printf '%s' "$COMMAND" | grep -qE '(rm|drop|delete|truncate).*(&&|\|\||;).*(rm|drop|delete)'; then
  printf '[BLOCKED] 위험한 복합 삭제 명령이 감지되었습니다.\n' >&2
  exit 2
fi

# ── 6. 시크릿 패턴 감지 ──────────────────────────────────────
if printf '%s' "$COMMAND" | grep -qiE '(password|secret|token|api.key)\s*=\s*["\x27][^"\x27]{8,}'; then
  printf '[WARNING] 명령에 시크릿 값이 포함된 것 같습니다. 환경변수를 사용하세요.\n' >&2
  # 경고만 하고 차단하지는 않음 (exit 0)
fi

exit 0