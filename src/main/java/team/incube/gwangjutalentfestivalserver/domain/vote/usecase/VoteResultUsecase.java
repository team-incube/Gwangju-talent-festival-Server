package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.response.VoteResultResponse;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class VoteResultUsecase {

    private final TeamRepository teamRepository;

    public VoteResultResponse execute(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        return new VoteResultResponse(
                teamId,
                team.getTeamName(),
                team.getStar()
        );
    }
}
