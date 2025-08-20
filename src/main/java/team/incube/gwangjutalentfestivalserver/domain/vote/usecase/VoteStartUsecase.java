package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class VoteStartUsecase {

    private final TeamRepository teamRepository;

    public void execute(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        if (team.getTeamStatus() != TeamStatus.PENDING) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "이미 투표가 시작되었거나 종료되었습니다.");
        }

        team.startVote();
        teamRepository.save(team);
    }
}
