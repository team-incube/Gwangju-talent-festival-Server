package team.incube.gwangjutalentfestivalserver.domain.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteResultResponse {
    private Long teamId;
    private String teamName;
    private int star;
}
