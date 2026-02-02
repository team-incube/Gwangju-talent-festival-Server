package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.response.VoteInviteResponse;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.VoteUser;
import team.incube.gwangjutalentfestivalserver.domain.vote.repository.VoteUserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteInviteUsecase {

    private final VoteUserRepository voteUserRepository;
    private final TeamRepository teamRepository;
    private final SeatReservationRepository seatReservationRepository;

    @Transactional(readOnly = true)
    public VoteInviteResponse execute(Long teamId, int limit) {
        if (limit <= 0) limit = 100;
        if (limit > 100) limit = 100;

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        List<VoteUser> candidates = voteUserRepository.findAllByTeamId(teamId);

        List<VoteUser> shuffled = new ArrayList<>(candidates);

        if (shuffled.isEmpty()) {
            return new VoteInviteResponse(team.getId(), team.getTeamName(), 0, List.of(), List.of());
        }

        Collections.shuffle(shuffled);
        List<VoteUser> picked = shuffled.stream().limit(limit).toList();

        List<UUID> userIds = picked.stream()
                .map(vu -> vu.getUser().getId())
                .toList();

        Map<UUID, SeatReservation> seatByUserId = seatReservationRepository.findAllByUser_IdIn(userIds).stream()
                .collect(Collectors.toMap(
                        sr -> sr.getUser().getId(),
                        sr -> sr,
                        (a, b) -> a
                ));

        List<GetSeatResponse> seats = picked.stream()
                .map(vu -> seatByUserId.get(vu.getUser().getId()))
                .filter(Objects::nonNull)
                .map(sr -> new GetSeatResponse(
                        String.valueOf(sr.getSeatSection()),
                        sr.getSeatNumber()
                ))
                .toList();

        List<String> phoneNumbers = picked.stream()
                .map(vu -> vu.getUser().getPhoneNumber())
                .toList();

        return new VoteInviteResponse(team.getId(), team.getTeamName(), picked.size(), seats, phoneNumbers);
    }
}