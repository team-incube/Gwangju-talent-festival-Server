package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InjectTeamUsecase {

    private final SeatReservationRepository seatReservationRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void assignTeamToReservations(Long teamId, List<SeatReservation> reservations) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        reservations.forEach(r -> r.setTeam(team));
        seatReservationRepository.saveAll(reservations);
    }
}
