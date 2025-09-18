package team.incube.gwangjutalentfestivalserver.domain.seat.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.BanSeatRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatBan;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.embeddable.SeatBanId;
import team.incube.gwangjutalentfestivalserver.domain.seat.event.SeatChangeEvent;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatBanRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

@Service
@RequiredArgsConstructor
public class BanSeatUsecase {
	private final SeatBanRepository seatBanRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public void execute(BanSeatRequest request) {
		SeatBanId seatBanId = request.toSeatBanId();

		boolean isAlreadyBannedSeat = seatBanRepository.existsById(seatBanId);
		if(isAlreadyBannedSeat) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "이미 관리자가 금지한 자리입니다.");
		}

		SeatBan newBan = new SeatBan(seatBanId, request.getRole());
		seatBanRepository.save(newBan);

		applicationEventPublisher.publishEvent(new SeatChangeEvent(
				request.getSeatSection(),
				request.getSeatNumber(),
				false
		));
	}
}
