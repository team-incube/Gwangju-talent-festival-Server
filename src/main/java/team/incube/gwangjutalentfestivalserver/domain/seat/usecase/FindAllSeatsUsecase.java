package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetAllSeatsResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatsBySectionResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatBan;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatBanRepository;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.global.util.SeatUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindAllSeatsUsecase {
    private final SeatBanRepository seatBanRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final SeatUtil seatUtil;

    private static final List<Character> SEAT_SECTIONS = List.of('A','B','C','D','E','F','G','H','I','J');

    public GetAllSeatsResponse execute() {
        List<SeatBan> seatBans = seatBanRepository.findAll();
        List<SeatReservation> seatReservations = seatReservationRepository.findAll();

        Map<Character, Set<SeatBan>> seatBansMap = seatBans.stream()
                .collect(Collectors.groupingBy(
                        sb -> sb.getId().getSeatSection(),
                        Collectors.toSet()
                ));

        Map<Character, Set<SeatReservation>> seatReservationMap = seatReservations.stream()
                .collect(Collectors.groupingBy(
                        SeatReservation::getSeatSection,
                        Collectors.toSet()
                ));

        Map<Character, GetSeatsBySectionResponse> responseMap = new HashMap<>();

        for (Character section : SEAT_SECTIONS) {
            Set<SeatBan> bans = seatBansMap.getOrDefault(section, Set.of());
            Set<SeatReservation> reservations = seatReservationMap.getOrDefault(section, Set.of());
            responseMap.put(section, getSeatResponse(section, bans, reservations));
        }

        return new GetAllSeatsResponse(
                responseMap.get(SEAT_SECTIONS.get(0)),
                responseMap.get(SEAT_SECTIONS.get(1)),
                responseMap.get(SEAT_SECTIONS.get(2)),
                responseMap.get(SEAT_SECTIONS.get(3)),
                responseMap.get(SEAT_SECTIONS.get(4)),
                responseMap.get(SEAT_SECTIONS.get(5)),
                responseMap.get(SEAT_SECTIONS.get(6)),
                responseMap.get(SEAT_SECTIONS.get(7)),
                responseMap.get(SEAT_SECTIONS.get(8)),
                responseMap.get(SEAT_SECTIONS.get(9))
        );

    }

    private GetSeatsBySectionResponse getSeatResponse(Character section,
                                                      Set<SeatBan> seatBans,
                                                      Set<SeatReservation> seatReservations) {
        int maxSeatNumber = seatUtil.getMaxSeats(section);

        Set<Integer> banned = seatBans.stream()
                .map(b -> b.getId().getSeatNumber())
                .collect(Collectors.toSet());

        Set<Integer> reserved = seatReservations.stream()
                .map(SeatReservation::getSeatNumber)
                .collect(Collectors.toSet());

        List<Boolean> seats = new ArrayList<>();
        for (int i = 1; i <= maxSeatNumber; i++) {
            boolean available = !(banned.contains(i) || reserved.contains(i));
            seats.add(available);
        }

        return new GetSeatsBySectionResponse(seats);
    }
}
