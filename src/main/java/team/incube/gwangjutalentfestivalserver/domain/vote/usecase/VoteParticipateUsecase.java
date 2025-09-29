package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.team.enums.TeamStatus;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.request.VoteParticipateRequest;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.Vote;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.embeddable.VoteId;
import team.incube.gwangjutalentfestivalserver.domain.vote.event.VoteChangeEvent;
import team.incube.gwangjutalentfestivalserver.domain.vote.repository.VoteRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.repository.TeamRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.repository.VoteUserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

@Service
@RequiredArgsConstructor
public class VoteParticipateUsecase {

    private final VoteRepository voteRepository;
    private final TeamRepository teamRepository;
    private final UserUtil userUtil;
    private final SeatReservationRepository seatReservationRepository;
    private final VoteUserRepository voteUserRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void execute(VoteParticipateRequest request) {
        User user = userUtil.getUser();

        if (!seatReservationRepository.existsByUser(user)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "예약된 좌석이 없습니다.");
        }

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."));

        if (!voteUserRepository.existsByTeamAndUser(team, user)) {
            throw new HttpException(HttpStatus.FORBIDDEN, "투표 자격이 없습니다.");
        }

        if (team.getTeamStatus() != TeamStatus.ONGOING) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "투표가 진행중이 아닙니다.");
        }

        Long maxId = voteRepository.findMaxIdByTeamId(team.getId()).orElse(0L);
        Long newId = maxId + 1;
        VoteId voteId = new VoteId(newId, team.getId());

        Vote vote = Vote.builder()
                .id(voteId)
                .team(team)
                .star(request.getStar())
                .build();

        voteRepository.save(vote);

        eventPublisher.publishEvent(new VoteChangeEvent(
                team.getId(),
                team.getTeamName(),
                request.getStar()
        ));
    }
}
