package team.incube.gwangjutalentfestivalserver.domain.seat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.BanSeatRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.CancelSeatBanRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.PerformerCancelSeatReservationRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.request.ReserveSeatRequest;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetAllSeatsResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatsBySectionResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.*;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.admin.BanSeatUsecase;
import team.incube.gwangjutalentfestivalserver.domain.seat.usecase.admin.CancelSeatBanUsecase;

import java.util.List;

@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
public class SeatController {
	private final ReserveSeatUsecase reserveSeatUsecase;
	private final CancelSeatReservationUsecase cancelSeatReservationUsecase;
	private final BanSeatUsecase banSeatUsecase;
	private final CancelSeatBanUsecase cancelSeatBanUsecase;
	private final ConnectSseSeatEventUsecase connectSseSeatEventUsecase;
	private final FindAllSeatsUsecase findAllSeatsUsecase;
	private final FindSeatsBySectionUsecase findSeatsBySectionUsecase;
	private final FindSeatByCurrentUserUsecase findSeatByCurrentUserUsecase;
    private final FindSeatsByCurrentPerformerUsecase findSeatsByCurrentPerformerUsecase;
    private final PerformerCancelSeatReservationUsecase performerCancelSeatReservationUsecase;

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

	@GetMapping("/myself")
	public ResponseEntity<GetSeatResponse> myself() {
		GetSeatResponse response = findSeatByCurrentUserUsecase.execute();
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<GetSeatsBySectionResponse> getBySection(@RequestParam String section) {
		GetSeatsBySectionResponse response = findSeatsBySectionUsecase.execute(section.charAt(0));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<GetAllSeatsResponse> getAllSeats() {
		GetAllSeatsResponse response = findAllSeatsUsecase.execute();
		return ResponseEntity.ok(response);
	}

    @GetMapping(value = "/changes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connectSeatChangeEvent() {
		return connectSseSeatEventUsecase.execute();
	}

    @GetMapping("/myself/performer")
    public ResponseEntity<List<GetSeatResponse>> getMyselfPerformerSeats() {
        List<GetSeatResponse> responses = findSeatsByCurrentPerformerUsecase.execute();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/performer")
    public ResponseEntity<Void> cancelMyselfPerformerSeats(@RequestBody PerformerCancelSeatReservationRequest request) {
        performerCancelSeatReservationUsecase.execute(request);
        return ResponseEntity.ok().build();
    }
}
