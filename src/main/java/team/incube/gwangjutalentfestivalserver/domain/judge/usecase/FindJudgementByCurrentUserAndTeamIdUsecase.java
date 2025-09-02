package team.incube.gwangjutalentfestivalserver.domain.judge.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.judge.dto.response.GetJudgementResponse;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.judge.repository.JudgementRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindJudgementByCurrentUserAndTeamIdUsecase {
    private final UserUtil userUtil;
    private final TeamRepository teamRepository;
    private final JudgementRepository judgementRepository;

    @Transactional(readOnly = true)
    public GetJudgementResponse execute(Long teamId) {
        User user = userUtil.getUser();

        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new HttpException(HttpStatus.NOT_FOUND, "해당하는 팀을 찾을 수 없습니다."));

        Judgement jm = judgementRepository.findByUserAndTeam(user, team).orElse(null);

        int completionExpression   = jm != null ? jm.getCompletionExpression() : 0;
        int creativityComposition  = jm != null ? jm.getCreativityComposition() : 0;
        int stageMannerPerformance = jm != null ? jm.getStageMannerPerformance() : 0;
        boolean judged             = jm != null;
        boolean performed          = team.getPerformStatus() != TeamStatus.PENDING;
        int totalScore             = Optional.ofNullable(team.getTotalScore()).orElse(0);

        return new GetJudgementResponse(
                jm != null ? jm.getId() : null,
                team.getId(),
                team.getTeamName(),
                completionExpression,
                creativityComposition,
                stageMannerPerformance,
                totalScore,
                performed,
                judged
        );
    }
}
