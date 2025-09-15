package team.incube.gwangjutalentfestivalserver.domain.vote.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.request.VoteParticipateRequest;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.response.VoteResultResponse;
import team.incube.gwangjutalentfestivalserver.domain.vote.usecase.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteController {

    private final VoteParticipateUsecase voteParticipateUsecase;
    private final VoteResultUsecase voteResultUsecase;
    private final VoteStartUsecase voteStartUsecase;
    private final VoteFinishUsecase voteFinishUsecase;
    private final ConnectSseVoteEventUsecase connectSseVoteEventUsecase;
    private final VoteRandomExtractUsecase voteRandomExtractUsecase;

    @PostMapping
    public ResponseEntity<Void> participate(@RequestBody @Valid VoteParticipateRequest request) {
        voteParticipateUsecase.execute(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<VoteResultResponse> getResult(@PathVariable Long teamId) {
        VoteResultResponse response = voteResultUsecase.execute(teamId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{teamId}")
    public ResponseEntity<Void> startVote(@PathVariable Long teamId) {
        voteStartUsecase.execute(teamId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> finishVote(@PathVariable Long teamId) {
        voteFinishUsecase.execute(teamId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value ="/{teamId}/current", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectVoteChangeEvent(@PathVariable Long teamId) {
        return connectSseVoteEventUsecase.execute(teamId);
    }

    @GetMapping("/{teamId}/extract")
    public ResponseEntity<byte[]> extractRandomVoters(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "100") int count
    ) {
        byte[] excelFile = voteRandomExtractUsecase.execute(teamId, count);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=voters_" + teamId + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}
