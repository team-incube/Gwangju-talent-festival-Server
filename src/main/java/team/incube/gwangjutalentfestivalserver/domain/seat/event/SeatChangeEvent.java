package team.incube.gwangjutalentfestivalserver.domain.seat.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.domain.seat.enums.SeatEventType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeatChangeEvent {
    private String seatSection;
    private Integer seatNumber;
    private SeatEventType seatEventType;
}
