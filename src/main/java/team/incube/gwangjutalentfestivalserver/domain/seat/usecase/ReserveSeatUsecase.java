package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.ReserveSeatRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.event.SeatChangeEvent;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatBanRepository;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.util.SeatUtil;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserveSeatUsecase {
	private final UserUtil userUtil;
	private final SeatReservationRepository seatReservationRepository;
	private final SeatBanRepository seatBanRepository;
	private final SeatUtil seatUtil;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public void execute(ReserveSeatRequest request) {
		Character seatSection = request.getSeatSection().charAt(0);
		Integer seatNumber = request.getSeatNumber();

		// 섹션 당 최대 좌석 수
		Integer maxSeats = seatUtil.getMaxSeats(seatSection);
		if(seatNumber > maxSeats) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "해당 섹션에 존재하지 않는 좌석입니다.");
		}

		// 해당 좌석이 이미 예약된 좌석인지
		Optional<SeatReservation> existsReservation =
			seatReservationRepository.findBySeatSectionAndSeatNumber(seatSection, seatNumber);

		if(existsReservation.isPresent()) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다.");
		}

		// 관리자가 금지한 좌석인지
		boolean isUnavailableSeat =
			seatBanRepository.existsById(request.toSeatBanId());

		if(isUnavailableSeat) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "관리자가 금지한 좌석입니다.");
		}

		// 이미 예약한 좌석이 존재하는지
		User currentUser = userUtil.getUser();
		long reserveCount = seatReservationRepository.countByUser(currentUser);

        int limit = switch (currentUser.getRole()) {
            case ROLE_PERFORMER -> 3;
            default -> 1;
        };

        if (reserveCount >= limit) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "예약 한도를 초과헀습니다.");
        }

		SeatReservation seatReservation = SeatReservation.builder()
			.seatNumber(seatNumber)
			.seatSection(seatSection)
			.user(currentUser)
			.build();

		seatReservationRepository.save(seatReservation);

		applicationEventPublisher.publishEvent(new SeatChangeEvent(
				seatSection.toString(),
				seatNumber,
				false
		));
	}
}
