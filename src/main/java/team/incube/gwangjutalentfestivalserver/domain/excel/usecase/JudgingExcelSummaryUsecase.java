package team.incube.gwangjutalentfestivalserver.domain.excel.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.excel.builder.JudgingSummaryExcelBuilder;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.judge.repository.JudgementRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JudgingExcelSummaryUsecase {

    private final TeamRepository teamRepository;
    private final JudgementRepository judgementRepository;

    /**
     * 집계표 엑셀 생성:
     * - A~F 각 심사위원의 100점 만점 점수(40+30+30) 표시
     * - 산출점수: 최저점과 최고점은 제외(유효 점수가 3개 미만이면 제외하지 않고 합계)
     * - 순위: 산출점수 기준 dense rank (동점 동일 순위, 다음 순위 건너뛰지 않음)
     */
    @Transactional(readOnly = true)
    public byte[] execute() {
        // 1) 팀 & 심사 전체 로드
        List<Team> teams = teamRepository.findAllByOrderByIdAsc();
        List<Judgement> judgements = judgementRepository.findAllWithUserAndTeam();

        // 2) 심사위원 고정 순서 A~F (UUID 문자열 기준 정렬해 안정적 매핑)
        List<UUID> judgeOrder = judgements.stream()
                .map(j -> j.getUser().getId())
                .distinct()
                .sorted(Comparator.comparing(UUID::toString))
                .limit(6)
                .toList();

        // 3) 팀별 × 심사위원별 100점 점수 맵 구성
        //    (완성도40 + 창의30 + 무대30)
        Map<Long, Map<UUID, Integer>> teamUserScore = new HashMap<>();
        for (Judgement j : judgements) {
            int total100 = nz(j.getCompletionExpression())
                    + nz(j.getCreativityComposition())
                    + nz(j.getStageMannerPerformance());
            teamUserScore
                    .computeIfAbsent(j.getTeam().getId(), k -> new HashMap<>())
                    .put(j.getUser().getId(), total100);
        }

        // 4) SummaryRow 생성 (개별 점수 + 산출점수)
        List<JudgingSummaryExcelBuilder.SummaryRow> rows = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);
            Map<UUID, Integer> byUser = teamUserScore.getOrDefault(t.getId(), Collections.emptyMap());

            // A~F 점수 채우기 (없는 사람은 null)
            List<Integer> judgeScores = new ArrayList<>(6);
            for (int k = 0; k < 6; k++) {
                Integer s = (k < judgeOrder.size()) ? byUser.get(judgeOrder.get(k)) : null;
                judgeScores.add(s);
            }

            // 산출점수 = 최저/최고 제외 합계 (유효 점수 3개 미만이면 그냥 합계)
            Integer total = trimmedSum(judgeScores);

            rows.add(new JudgingSummaryExcelBuilder.SummaryRow(
                    String.valueOf(i + 1),
                    judgeScores,
                    total,
                    null // 순위는 아래에서 채움
            ));
        }

        // 5) 순위(dense rank) 계산
        Map<Integer, Integer> rankMap = denseRank(
                rows.stream().map(JudgingSummaryExcelBuilder.SummaryRow::total).toList()
        );
        rows = rows.stream()
                .map(r -> new JudgingSummaryExcelBuilder.SummaryRow(
                        r.no(), r.judgeScores(), r.total(),
                        r.total() == null ? null : rankMap.get(r.total())
                ))
                .toList();

        // 6) 엑셀 생성
        String title = "2025학년도 『光탈페(광주학생탈렌트페스티벌)』 본선 심사위원 심사집계표";
        return JudgingSummaryExcelBuilder.build(title, rows);
    }

    private int nz(Integer v) { return v == null ? 0 : v; }

    /**
     * 최저/최고 제외 합계
     * - 유효 점수(Null 제외)가 3개 이상이면 min/max를 제외하고 합계
     * - 유효 점수 0개: null 반환
     * - 유효 점수 1~2개: 제외하지 않고 합계
     */
    private Integer trimmedSum(List<Integer> scores) {
        List<Integer> valid = scores.stream().filter(Objects::nonNull).toList();
        if (valid.isEmpty()) return null;
        if (valid.size() < 3) return valid.stream().reduce(0, Integer::sum);

        int min = valid.stream().min(Integer::compareTo).orElse(0);
        int max = valid.stream().max(Integer::compareTo).orElse(0);
        int sum = valid.stream().reduce(0, Integer::sum);
        return sum - min - max;
    }

    /** 합계 점수별 dense rank 계산 (동점 동일 순위, null 제외) */
    private Map<Integer, Integer> denseRank(List<Integer> totals) {
        List<Integer> distinctDesc = totals.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();
        Map<Integer, Integer> rank = new HashMap<>();
        for (int i = 0; i < distinctDesc.size(); i++) {
            rank.put(distinctDesc.get(i), i + 1);
        }
        return rank;
    }
}