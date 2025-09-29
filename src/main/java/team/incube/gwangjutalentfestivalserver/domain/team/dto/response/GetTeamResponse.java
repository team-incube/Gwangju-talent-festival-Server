package team.incube.gwangjutalentfestivalserver.domain.team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamResponse {
    private Long id;
    private String name;
}
