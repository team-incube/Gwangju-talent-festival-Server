package team.incube.gwangjutalentfestivalserver.domain.judge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "judgement",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "team_id", "user_id" })
        }
)
@Check(constraints = "completion_expression BETWEEN 0 AND 40 " +
        "AND creativity_composition BETWEEN 0 AND 30 " +
        "AND stage_manner_performance BETWEEN 0 AND 30")
@Builder
public class Judgement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "completion_expression", nullable = false)
    private Integer completionExpression;

    @Column(name = "creativity_composition", nullable = false)
    private Integer creativityComposition;

    @Column(name = "stage_manner_performance", nullable = false)
    private Integer stageMannerPerformance;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateScore(int completionExpression, int creativityComposition, int stageMannerPerformance) {
        this.completionExpression = completionExpression;
        this.creativityComposition = creativityComposition;
        this.stageMannerPerformance = stageMannerPerformance;
    }
}
