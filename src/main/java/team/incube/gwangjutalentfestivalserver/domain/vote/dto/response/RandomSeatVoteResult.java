package team.incube.gwangjutalentfestivalserver.domain.vote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;

import java.util.List;

@Getter
@AllArgsConstructor
public class RandomSeatVoteResult {
    private List<GetSeatResponse> seats;
    private byte[] excelBytes;
}

