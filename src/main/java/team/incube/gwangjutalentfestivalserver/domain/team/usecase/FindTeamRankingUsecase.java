package team.incube.gwangjutalentfestivalserver.domain.team.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.TeamRankingDto;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.response.GetTeamRankingResponse;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTeamRankingUsecase {
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<GetTeamRankingResponse> execute() {
        Integer toYear = LocalDateTime.now().getYear();

        List<TeamRankingDto> rankingDtos = teamRepository.findAllByEventYearWithRanking(toYear);
        List<String> popularityAwardTeams = teamRepository.findTop2TeamsByStar(toYear);

        java.util.Set<String> popularSet = new java.util.HashSet<>(popularityAwardTeams);

        return rankingDtos.stream()
                .map(dto -> new GetTeamRankingResponse(
                        dto.getRanking(),
                        dto.getTeamName(),
                        popularSet.contains(dto.getTeamName())
                ))
                .toList();
    }
}
