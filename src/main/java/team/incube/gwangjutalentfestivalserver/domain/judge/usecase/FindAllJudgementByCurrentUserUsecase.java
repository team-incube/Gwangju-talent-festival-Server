package team.incube.gwangjutalentfestivalserver.domain.judge.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.judge.dto.response.GetAllJudgementsResponse;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.judge.repository.JudgementRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllJudgementByCurrentUserUsecase {
    private final UserUtil userUtil;
    private final TeamRepository teamRepository;
    private final JudgementRepository judgementRepository;

    @Transactional(readOnly = true)
    public List<GetAllJudgementsResponse> execute() {
        User user = userUtil.getUser();

        List<Judgement> judgements = judgementRepository.findAllByUser(user);

        Integer toYear = LocalDateTime.now().getYear();
        List<Team> teams = teamRepository.findAllByEventYear(toYear);

        Map<Long, Judgement> judgementMap = judgements.stream()
                .collect(Collectors.toMap(j -> j.getTeam().getId(), j -> j));

        List<GetAllJudgementsResponse> responses = new ArrayList<>();

        for (Team team : teams) {
            Judgement jm = judgementMap.get(team.getId());

            int completionExpression   = jm != null ? jm.getCompletionExpression() : 0;
            int creativityComposition  = jm != null ? jm.getCreativityComposition() : 0;
            int stageMannerPerformance = jm != null ? jm.getStageMannerPerformance() : 0;
            boolean judged             = jm != null;
            boolean performed          = team.getPerformStatus() != TeamStatus.PENDING;
            int totalScore             = Optional.ofNullable(team.getTotalScore()).orElse(0);

            responses.add(new GetAllJudgementsResponse(
                    jm != null ? jm.getId() : null,
                    team.getId(),
                    team.getTeamName(),
                    completionExpression,
                    creativityComposition,
                    stageMannerPerformance,
                    totalScore,
                    performed,
                    judged
            ));
        }

        return responses;
    }
}
