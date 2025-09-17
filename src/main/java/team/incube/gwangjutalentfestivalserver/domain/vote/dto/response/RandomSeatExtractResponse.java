package team.incube.gwangjutalentfestivalserver.domain.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RandomSeatExtractResponse {
    private Long teamId;
    private String teamName;
    private List<GetSeatResponse> seats;
}
