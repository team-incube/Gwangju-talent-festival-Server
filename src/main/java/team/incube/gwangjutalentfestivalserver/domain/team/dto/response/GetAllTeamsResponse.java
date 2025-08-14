package team.incube.gwangjutalentfestivalserver.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllTeamsResponse {
    private Long teamId;
    private String teamName;
    private Integer star;
    private TeamStatus voteStatus;

    public static GetAllTeamsResponse of(Long teamId, String teamName, Integer star, TeamStatus voteStatus) {
        return new GetAllTeamsResponse(teamId, teamName, star, voteStatus);
    }
}
