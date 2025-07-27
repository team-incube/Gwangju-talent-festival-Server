package team.incube.gwangjutalentfestivalserver.domain.seat.usecase.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.CancelSeatBanRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatBan;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.embeddable.SeatBanId;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatBanRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CancelSeatBanUsecase {
	private final SeatBanRepository seatBanRepository;

	@Transactional
	public void execute(CancelSeatBanRequest request) {
		SeatBanId seatBanId = request.toSeatBanId();
		SeatBan seatBan = seatBanRepository.findById(seatBanId).orElseThrow(() ->
			new HttpException(HttpStatus.BAD_REQUEST, "이미 금지되지 않은 상태의 자리입니다.")
		);

		seatBanRepository.delete(seatBan);
	}
}
