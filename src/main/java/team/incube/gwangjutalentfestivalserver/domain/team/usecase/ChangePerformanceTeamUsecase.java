package team.incube.gwangjutalentfestivalserver.domain.team.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.judge.event.JudgementTeamEvent;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePerformanceTeamUsecase {
    private final TeamRepository teamRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void execute(Long teamId) {
        log.info("Team {} performance status changed to ONGOING", teamId);
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new HttpException(HttpStatus.NOT_FOUND, "해당하는 팀을 찾을 수 없습니다."));

        TeamStatus perform = team.getPerformStatus();
        if (perform == TeamStatus.FINISHED || perform == TeamStatus.ONGOING) {
            throw new HttpException(HttpStatus.CONFLICT, "공연이 끝났거나 진행중인 팀입니다.");
        }

        team.updatePerformStatus(TeamStatus.ONGOING);

        applicationEventPublisher.publishEvent(new JudgementTeamEvent(teamId));
    }
}
