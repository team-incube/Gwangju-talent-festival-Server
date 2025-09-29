package team.incube.gwangjutalentfestivalserver.domain.vote.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.embeddable.VoteId;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "votes")
@AllArgsConstructor
@Builder
public class Vote {
    @EmbeddedId
    private VoteId id;

    @Column(nullable = false)
    private int star;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
