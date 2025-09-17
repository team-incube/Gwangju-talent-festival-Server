package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.TeamMembership;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamMembershipRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RandomReservationExtractor {

    private final TeamMembershipRepository teamMembershipRepository;
    private final SeatReservationRepository seatReservationRepository;

    @Transactional
    public List<SeatReservation> extractRandomReservations(Long teamId, int count) {
        List<TeamMembership> memberships = teamMembershipRepository.findAllByTeamId(teamId);

        if (memberships.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "해당 팀에 소속된 유저가 없습니다.");
        }

        List<UUID> userIds = memberships.stream()
                .map(m -> m.getUser().getId())
                .toList();

        List<SeatReservation> reservations = seatReservationRepository.findAllByUserIdIn(userIds);

        if (reservations.isEmpty()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "해당 팀에 예약된 좌석이 없습니다.");
        }

        Collections.shuffle(reservations);
        return reservations.stream()
                .limit(count)
                .toList();
    }
}
