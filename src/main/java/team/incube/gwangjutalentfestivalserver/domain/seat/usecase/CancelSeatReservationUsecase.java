package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancelSeatReservationUsecase {
	private final UserUtil userUtil;
	private final SeatReservationRepository seatReservationRepository;

	@Transactional
	public void execute() {
		User currentUser = userUtil.getUser();
		Optional<SeatReservation> seatReservation =
			seatReservationRepository.findByUser(currentUser);

		if(seatReservation.isEmpty()) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "예약된 좌석이 없습니다.");
		}

		seatReservationRepository.delete(seatReservation.get());
	}
}
