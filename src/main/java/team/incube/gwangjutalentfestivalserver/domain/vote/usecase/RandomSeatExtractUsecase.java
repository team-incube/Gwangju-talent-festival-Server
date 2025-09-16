package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.response.RandomSeatExtractResponse;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomSeatExtractUsecase {

    private final TeamRepository teamRepository;
    private final RandomReservationExtractor extractor;

    @Transactional(readOnly = true)
    public RandomSeatExtractResponse execute(Long teamId, int count) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        List<SeatReservation> reservations = extractor.extractRandomReservations(teamId, count, team);

        List<GetSeatResponse> seats = reservations.stream()
                .map(r -> new GetSeatResponse(
                        String.valueOf(r.getSeatSection()),
                        r.getSeatNumber()
                ))
                .toList();

        return new RandomSeatExtractResponse(
                team.getId(),
                team.getTeamName(),
                seats
        );
    }
}

