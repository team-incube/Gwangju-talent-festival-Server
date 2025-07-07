package team.incube.gwangjutalentfestivalserver.domain.seat.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;

import java.util.Optional;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SeatReservation> findBySeatSectionAndSeatNumber(Character seatSection, Integer seatNumber);

    Boolean existsByUser(User user);

    Optional<SeatReservation> findByUser(User user);

    void deleteByUser(User user);
}
