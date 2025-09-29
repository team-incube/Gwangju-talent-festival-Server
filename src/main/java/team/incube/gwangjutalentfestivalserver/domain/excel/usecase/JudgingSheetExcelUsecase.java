package team.incube.gwangjutalentfestivalserver.domain.excel.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.excel.builder.JudgingSheetExcelBuilder;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.judge.repository.JudgementRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class JudgingSheetExcelUsecase {

    private final TeamRepository teamRepository;
    private final JudgementRepository judgementRepository;

    @Transactional(readOnly = true)
    public byte[] execute() {
        List<Team> teams = teamRepository.findAllByOrderByIdAsc();

        List<Judgement> judgements = judgementRepository.findAllWithUserAndTeam();

        Map<Object, List<Judgement>> byUserId = judgements.stream()
                .collect(Collectors.groupingBy(j -> j.getUser().getId(), LinkedHashMap::new, Collectors.toList()));

        List<JudgingSheetExcelBuilder.ScoreRow> blankRows = IntStream.range(0, teams.size())
                .mapToObj(i -> new JudgingSheetExcelBuilder.ScoreRow(String.valueOf(i + 1), teams.get(i).getTeamName(), null, null, null))
                .toList();

        String[] suffix = {"A","B","C","D","E","F"};
        List<JudgingSheetExcelBuilder.SheetSpec> sheets = new ArrayList<>();
        int idx = 0;

        for (Map.Entry<Object, List<Judgement>> e : byUserId.entrySet()) {
            if (idx >= suffix.length) break;

            Map<Long, Judgement> byTeamId = e.getValue().stream()
                    .collect(Collectors.toMap(j -> j.getTeam().getId(), j -> j));

            List<JudgingSheetExcelBuilder.ScoreRow> rows = IntStream.range(0, teams.size())
                    .mapToObj(i -> {
                        Team t = teams.get(i);
                        Judgement j = byTeamId.get(t.getId());
                        return new JudgingSheetExcelBuilder.ScoreRow(
                                String.valueOf(i + 1),
                                t.getTeamName(),
                                j == null ? null : j.getCompletionExpression(),
                                j == null ? null : j.getCreativityComposition(),
                                j == null ? null : j.getStageMannerPerformance()
                        );
                    })
                    .toList();

            sheets.add(new JudgingSheetExcelBuilder.SheetSpec("심사표" + suffix[idx], rows));
            idx++;
        }

        while (idx < suffix.length) {
            sheets.add(new JudgingSheetExcelBuilder.SheetSpec("심사표" + suffix[idx], blankRows));
            idx++;
        }

        String title = "2025학년도 『光학생탤런트페스티벌』 본선 심사위원 개인별 심사표";
        return JudgingSheetExcelBuilder.buildMulti(title, sheets);
    }
}