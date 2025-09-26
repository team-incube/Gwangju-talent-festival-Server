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

    @Transactional(readOnly = true)
    public byte[] execute() {
        List<Team> teams = teamRepository.findAllByOrderByIdAsc();
        List<Judgement> judgements = judgementRepository.findAllWithUserAndTeam();

        List<UUID> judgeOrder = judgements.stream()
                .map(j -> j.getUser().getId())
                .distinct()
                .sorted(Comparator.comparing(UUID::toString))
                .limit(6)
                .toList();

        Map<Long, Map<UUID, Integer>> teamUserScore = new HashMap<>();
        for (Judgement j : judgements) {
            int total100 = nz(j.getCompletionExpression())
                    + nz(j.getCreativityComposition())
                    + nz(j.getStageMannerPerformance());
            teamUserScore
                    .computeIfAbsent(j.getTeam().getId(), k -> new HashMap<>())
                    .put(j.getUser().getId(), total100);
        }

        List<JudgingSummaryExcelBuilder.SummaryRow> rows = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);
            Map<UUID, Integer> byUser = teamUserScore.getOrDefault(t.getId(), Collections.emptyMap());

            List<Integer> judgeScores = new ArrayList<>(6);
            for (int k = 0; k < 6; k++) {
                Integer s = (k < judgeOrder.size()) ? byUser.get(judgeOrder.get(k)) : null;
                judgeScores.add(s);
            }

            Integer total = trimmedSum(judgeScores);

            rows.add(new JudgingSummaryExcelBuilder.SummaryRow(
                    String.valueOf(i + 1),
                    judgeScores,
                    total,
                    null
            ));
        }

        Map<Integer, Integer> rankMap = denseRank(
                rows.stream().map(JudgingSummaryExcelBuilder.SummaryRow::total).toList()
        );
        rows = rows.stream()
                .map(r -> new JudgingSummaryExcelBuilder.SummaryRow(
                        r.no(), r.judgeScores(), r.total(),
                        r.total() == null ? null : rankMap.get(r.total())
                ))
                .toList();


        String title = "2025학년도 『光탈페(광주학생탈렌트페스티벌)』 본선 심사위원 심사집계표";
        return JudgingSummaryExcelBuilder.build(title, rows);
    }

    private int nz(Integer v) { return v == null ? 0 : v; }

    private Integer trimmedSum(List<Integer> scores) {
        List<Integer> valid = scores.stream().filter(Objects::nonNull).toList();
        if (valid.isEmpty()) return null;
        if (valid.size() < 3) return valid.stream().reduce(0, Integer::sum);

        int min = valid.stream().min(Integer::compareTo).orElse(0);
        int max = valid.stream().max(Integer::compareTo).orElse(0);
        int sum = valid.stream().reduce(0, Integer::sum);
        return sum - min - max;
    }

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