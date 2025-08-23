package team.incube.gwangjutalentfestivalserver.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamRankingResponse {
    private Integer ranking;
    private String teamName;
    private boolean popularityAward;
}
