package team.incube.gwangjutalentfestivalserver.domain.team.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.request.CreateTeamRequest;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.response.GetAllTeamsResponse;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.response.GetTeamRankingResponse;
import team.incube.gwangjutalentfestivalserver.domain.team.usecase.ChangePerformanceTeamUsecase;
import team.incube.gwangjutalentfestivalserver.domain.team.usecase.CreateTeamUsecase;
import team.incube.gwangjutalentfestivalserver.domain.team.usecase.FindAllTeamsUsecase;
import team.incube.gwangjutalentfestivalserver.domain.team.usecase.FindTeamRankingUsecase;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final FindAllTeamsUsecase findAllTeamsUsecase;
    private final ChangePerformanceTeamUsecase changePerformanceTeamUsecase;
    private final CreateTeamUsecase createTeamUsecase;
    private final FindTeamRankingUsecase findTeamRankingUsecase;

    @GetMapping
    public ResponseEntity<List<GetAllTeamsResponse>> getAllTeams() {
        List<GetAllTeamsResponse> responses = findAllTeamsUsecase.execute();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{team_id}")
    public ResponseEntity<Void> changePerformanceTeam(@PathVariable("team_id") Long teamId) {
        changePerformanceTeamUsecase.execute(teamId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createTeam(@RequestBody @Valid CreateTeamRequest request) {
        createTeamUsecase.execute(request.getTeamName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<GetTeamRankingResponse>> getTeamRanking() {
        List<GetTeamRankingResponse> responses = findTeamRankingUsecase.execute();
        return ResponseEntity.ok(responses);
    }
}
