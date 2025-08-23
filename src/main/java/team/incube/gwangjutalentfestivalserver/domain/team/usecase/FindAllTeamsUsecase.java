package team.incube.gwangjutalentfestivalserver.domain.team.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.response.GetAllTeamsResponse;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllTeamsUsecase {
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<GetAllTeamsResponse> execute() {
        LocalDate today = LocalDate.now();
        List<Team> teams = teamRepository.findAllByEventYear(today.getYear());

        return teams.stream()
                .map(t -> GetAllTeamsResponse.of(
                        t.getId(),
                        t.getTeamName(),
                        t.getStar(),
                        t.getTeamStatus())
                ).toList();
    }
}