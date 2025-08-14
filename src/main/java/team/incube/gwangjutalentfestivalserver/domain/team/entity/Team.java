package team.incube.gwangjutalentfestivalserver.domain.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "team")
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamStatus teamStatus;

    @Column(name = "event_year", nullable = false)
    private Integer eventYear;

    @Column(name = "star", nullable = false)
    private Integer star;
}
