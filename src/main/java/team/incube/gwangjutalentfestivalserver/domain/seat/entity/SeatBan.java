package team.incube.gwangjutalentfestivalserver.domain.seat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.embeddable.SeatBanId;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;

@Table(name = "seat_ban")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatBan {
    @EmbeddedId
    private SeatBanId id;
}
