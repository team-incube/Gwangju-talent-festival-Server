package team.incube.gwangjutalentfestivalserver.domain.judge.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.domain.judge.dto.request.SaveJudgementRequest;
import team.incube.gwangjutalentfestivalserver.domain.judge.dto.response.GetAllJudgementsResponse;
import team.incube.gwangjutalentfestivalserver.domain.judge.dto.response.GetJudgementResponse;
import team.incube.gwangjutalentfestivalserver.domain.judge.usecase.ConnectSseJudgeEventUsecase;
import team.incube.gwangjutalentfestivalserver.domain.judge.usecase.FindAllJudgementByCurrentUserUsecase;
import team.incube.gwangjutalentfestivalserver.domain.judge.usecase.FindJudgementByCurrentUserAndTeamIdUsecase;
import team.incube.gwangjutalentfestivalserver.domain.judge.usecase.SaveJudgementUsecase;

import java.util.List;

@RestController
@RequestMapping("/judge")
@RequiredArgsConstructor
public class JudgementController {
    private final SaveJudgementUsecase saveJudgementUsecase;
    private final FindAllJudgementByCurrentUserUsecase findAllJudgementByCurrentUserUsecase;
    private final FindJudgementByCurrentUserAndTeamIdUsecase findJudgementByCurrentUserAndTeamIdUsecase;
    private final ConnectSseJudgeEventUsecase connectSseJudgeEventUsecase;

    @PatchMapping("/{team_id}")
    public ResponseEntity<Void> judgement(
            @PathVariable("team_id") Long teamId,
            @Valid @RequestBody SaveJudgementRequest request
    ) {
        saveJudgementUsecase.execute(
                request.getCompletionExpression(),
                request.getCreativityComposition(),
                request.getStageMannerPerformance(),
                teamId
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GetAllJudgementsResponse>> getAllTeamsJudgement() {
        List<GetAllJudgementsResponse> response = findAllJudgementByCurrentUserUsecase.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{team_id}")
    public ResponseEntity<GetJudgementResponse> getTeamJudgement(@PathVariable("team_id") Long teamId) {
        GetJudgementResponse response = findJudgementByCurrentUserAndTeamIdUsecase.execute(teamId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/changes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectJudgementChangeEvent() {
        return connectSseJudgeEventUsecase.execute();
    }
}
