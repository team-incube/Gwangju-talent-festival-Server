package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.Vote;
import team.incube.gwangjutalentfestivalserver.domain.vote.repository.VoteRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.event.VoteFinishEvent;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

@Service
@RequiredArgsConstructor
public class VoteFinishUsecase {

    private final TeamRepository teamRepository;
    private final VoteRepository voteRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void execute(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        if (team.getTeamStatus() != TeamStatus.ONGOING) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "투표가 진행중이 아닙니다.");
        }

        int totalStar = voteRepository.findByTeamId(teamId)
                .stream()
                .mapToInt(Vote::getStar)
                .sum();

        team.finishVote();
        team.setStar(totalStar);
        teamRepository.save(team);

        eventPublisher.publishEvent(new VoteFinishEvent(
                team.getId(),
                team.getTeamName(),
                totalStar
        ));
    }
}
