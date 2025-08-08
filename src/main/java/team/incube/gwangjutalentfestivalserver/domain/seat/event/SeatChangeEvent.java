package team.incube.gwangjutalentfestivalserver.domain.seat.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatChangeEvent {
    private String seatSection;
    private Integer seatNumber;
    private Boolean isAvailable;
}
