package team.incube.gwangjutalentfestivalserver.domain.seat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;

@Table(
    name = "seat_reservation",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "seat_section", "seat_number" })
    },
    indexes = {
        @Index(name = "idx_seat_section_number", columnList = "seat_section, seat_number")
    }
)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_section", nullable = false)
    private Character seatSection;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
