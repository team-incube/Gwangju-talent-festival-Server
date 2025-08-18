package team.incube.gwangjutalentfestivalserver.domain.vote.dto.response;

public record VoteResultResponse(
        Long teamId,
        String teamName,
        int star
) {
}
