package team.incube.gwangjutalentfestivalserver.domain.vote.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class VoteId {
    private Long id;
    private Long teamId;
}
