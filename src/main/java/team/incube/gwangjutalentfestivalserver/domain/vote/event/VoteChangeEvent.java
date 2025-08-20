package team.incube.gwangjutalentfestivalserver.domain.vote.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteChangeEvent {
    private Long teamId;
    private String teamName;
    private int addStar;
}
