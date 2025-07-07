package team.incube.gwangjutalentfestivalserver.domain.seat.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SeatBanId implements Serializable {
    private Character seatSection;
    private Integer seatNumber;
}
