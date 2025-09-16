package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.PerformerCancelSeatReservationRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.event.SeatChangeEvent;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

@Service
@RequiredArgsConstructor
public class PerformerCancelSeatReservationUsecase {
    private final UserUtil userUtil;
    private final SeatReservationRepository seatReservationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void execute(PerformerCancelSeatReservationRequest request) {
        User currentUser = userUtil.getUser();
        SeatReservation seatReservation =
                seatReservationRepository.findBySeatSectionAndSeatNumberAndUser(
                        request.getSeatSection().charAt(0),
                        request.getSeatNumber(),
                        currentUser
                ).orElseThrow(() ->
                        new HttpException(HttpStatus.NOT_FOUND, "해당하는 예약된 좌석을 찾을 수 없습니다.")
                );

        seatReservationRepository.delete(seatReservation);

        applicationEventPublisher.publishEvent(new SeatChangeEvent(
                seatReservation.getSeatSection().toString(),
                seatReservation.getSeatNumber(),
                false
        ));
    }
}
