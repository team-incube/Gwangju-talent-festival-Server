package team.incube.gwangjutalentfestivalserver.domain.judge.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.judge.repository.JudgementRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

@Service
@RequiredArgsConstructor
public class SaveJudgementUsecase {
    private final UserUtil userUtil;
    private final TeamRepository teamRepository;
    private final JudgementRepository judgementRepository;

    @Transactional
    public void execute(
            int completionExpression,
            int creativityComposition,
            int stageMannerPerformance,
            Long teamId)
    {
        User user = userUtil.getUser();
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new HttpException(HttpStatus.NOT_FOUND, "해당하는 팀을 찾을 수 없습니다.")
        );

        Judgement judgement = judgementRepository.findByUserAndTeam(user, team)
                .orElseGet(() -> Judgement.builder()
                        .team(team)
                        .user(user)
                        .build());

        judgement.updateScore(completionExpression, creativityComposition, stageMannerPerformance);

        judgementRepository.save(judgement);
    }
}
