package team.incube.gwangjutalentfestivalserver.domain.team.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTeamUsecase {
    private final TeamRepository teamRepository;

    @Transactional
    public void execute(String teamName) {
        Integer toYear = LocalDate.now().getYear();
        Team team = Team.builder()
                .teamName(teamName)
                .teamStatus(TeamStatus.PENDING)
                .performStatus(TeamStatus.PENDING)
                .eventYear(toYear)
                .star(0)
                .totalScore(0)
                .build();

        teamRepository.save(team);
    }
}
