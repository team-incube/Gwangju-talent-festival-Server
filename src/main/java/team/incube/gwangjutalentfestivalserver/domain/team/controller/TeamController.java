package team.incube.gwangjutalentfestivalserver.domain.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.response.GetAllTeamsResponse;
import team.incube.gwangjutalentfestivalserver.domain.team.usecase.FindAllTeamsUsecase;

import java.util.List;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    private final FindAllTeamsUsecase findAllTeamsUsecase;

    @GetMapping
    public ResponseEntity<List<GetAllTeamsResponse>> getAllTeams() {
        List<GetAllTeamsResponse> responses = findAllTeamsUsecase.execute();
        return ResponseEntity.ok(responses);
    }
}
