package team.incube.gwangjutalentfestivalserver.domain.seat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllSeatsResponse {
    private GetSeatsBySectionResponse sectionA;
    private GetSeatsBySectionResponse sectionB;
    private GetSeatsBySectionResponse sectionC;
    private GetSeatsBySectionResponse sectionD;
    private GetSeatsBySectionResponse sectionE;
    private GetSeatsBySectionResponse sectionF;
    private GetSeatsBySectionResponse sectionG;
    private GetSeatsBySectionResponse sectionH;
    private GetSeatsBySectionResponse sectionI;
    private GetSeatsBySectionResponse sectionJ;
}
