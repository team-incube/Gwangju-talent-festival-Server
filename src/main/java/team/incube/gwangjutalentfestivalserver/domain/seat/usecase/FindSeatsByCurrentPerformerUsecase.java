package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindSeatsByCurrentPerformerUsecase {
    private final UserUtil userUtil;
    private final SeatReservationRepository seatReservationRepository;

    public List<GetSeatResponse> execute() {
        User user = userUtil.getUser();

        List<SeatReservation> seatReservations = seatReservationRepository.findAllByUser(user);

        return seatReservations.stream()
                .map(sr ->
                        new GetSeatResponse(
                                sr.getSeatSection().toString(),
                                sr.getSeatNumber()))
                .toList();
    }
}
