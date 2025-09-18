package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatsBySectionResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatBan;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatBanRepository;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;
import team.incube.gwangjutalentfestivalserver.global.util.SeatUtil;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindSeatsBySectionUsecase {
    private final UserUtil userUtil;
    private final SeatReservationRepository seatReservationRepository;
    private final SeatBanRepository seatBanRepository;
    private final SeatUtil seatUtil;

    @Transactional(readOnly = true)
    public GetSeatsBySectionResponse execute(Character section) {
        Role role = userUtil.getUser().getRole();
        Integer seatLastNumber = seatUtil.getMaxSeats(section);

        List<SeatBan> seatBans = seatBanRepository.findById_SeatSectionAndRole(section, role);
        List<SeatReservation> seatReservations = seatReservationRepository.findBySeatSection(section);

        Set<Integer> bannedSeatNumbers = seatBans.stream()
                .map(ban -> ban.getId().getSeatNumber())
                .collect(Collectors.toSet());

        Set<Integer> reservedSeatNumbers = seatReservations.stream()
                .map(SeatReservation::getSeatNumber)
                .collect(Collectors.toSet());

        List<Boolean> seats = new ArrayList<>();
        for (int i = 1; i <= seatLastNumber; i++) {
            boolean available = !(!bannedSeatNumbers.contains(i) || reservedSeatNumbers.contains(i));
            seats.add(available);
        }

        return new GetSeatsBySectionResponse(seats);
    }
}
