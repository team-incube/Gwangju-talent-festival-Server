package team.incube.gwangjutalentfestivalserver.domain.seat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.BanSeatRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.CancelSeatBanRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.ReserveSeatRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.CancelSeatReservationUsecase;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.ReserveSeatUsecase;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.admin.BanSeatUsecase;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.admin.CancelSeatBanUsecase;

@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
public class SeatController {
	private final ReserveSeatUsecase reserveSeatUsecase;
	private final CancelSeatReservationUsecase cancelSeatReservationUsecase;
	private final BanSeatUsecase banSeatUsecase;
	private final CancelSeatBanUsecase cancelSeatBanUsecase;

	@PostMapping
	public ResponseEntity<Void> reserveSeat(
		@Valid @RequestBody ReserveSeatRequest reserveSeatRequest
	){
		reserveSeatUsecase.execute(reserveSeatRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> cancelSeatReservation(){
		cancelSeatReservationUsecase.execute();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/ban")
	public ResponseEntity<Void> banSeat(
		@Valid @RequestBody BanSeatRequest banSeatRequest
	){
		banSeatUsecase.execute(banSeatRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/ban")
	public ResponseEntity<Void> cancelSeatBan(
		@Valid @RequestBody CancelSeatBanRequest cancelSeatBanRequest
	){
		cancelSeatBanUsecase.execute(cancelSeatBanRequest);
		return ResponseEntity.ok().build();
	}
}
