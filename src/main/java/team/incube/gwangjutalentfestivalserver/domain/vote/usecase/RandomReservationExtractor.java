package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RandomReservationExtractor {

    private final SeatReservationRepository seatReservationRepository;

    public List<SeatReservation> extractRandomReservations(Long teamId, int count, Team team) {
        List<SeatReservation> reservations = seatReservationRepository.findAllByTeamId(teamId);

        if (reservations.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "해당 팀에는 좌석 예약자가 없습니다.");
        }

        List<SeatReservation> limited = reservations.stream()
                .limit(count)
                .toList();

        limited.forEach(r -> r.setTeam(team));
        seatReservationRepository.saveAll(limited);

        return limited;
    }
}

