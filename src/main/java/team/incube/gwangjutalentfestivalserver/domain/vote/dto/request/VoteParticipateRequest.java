package team.incube.gwangjutalentfestivalserver.domain.vote.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VoteParticipateRequest(
        @NotNull Long teamId,
        @Min(0) @Max(3) int star
) {}
