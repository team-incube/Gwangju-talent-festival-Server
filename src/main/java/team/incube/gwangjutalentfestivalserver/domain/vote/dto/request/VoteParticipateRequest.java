package team.incube.gwangjutalentfestivalserver.domain.vote.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteParticipateRequest {
    @NotNull
    private Long teamId;

    @Min(0) @Max(3)
    private int star;
}
