package team.incube.gwangjutalentfestivalserver.domain.seat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetSeatsBySectionResponse {
    private List<Boolean> seats;
}
