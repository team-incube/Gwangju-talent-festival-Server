package team.incube.gwangjutalentfestivalserver.domain.team.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.response.GetTeamResponse;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

@Service
@RequiredArgsConstructor
public class FindTeamByIdUsecase {

    private final TeamRepository teamRepository;

    public GetTeamResponse execute(Long teamId) {
        GetTeamResponse response = teamRepository.findById(teamId)
                .map(t -> new GetTeamResponse(t.getId(), t.getTeamName()))
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당하는 팀을 찾을 수 없습니다."));
        return response;
    }
}
