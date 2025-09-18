package team.incube.gwangjutalentfestivalserver.domain.seat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatBan;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.embeddable.SeatBanId;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;

import java.util.List;
import java.util.Optional;

public interface SeatBanRepository extends JpaRepository<SeatBan, SeatBanId> {
    boolean existsById(SeatBanId id);

    Optional<SeatBan> findById(SeatBanId id);

    List<SeatBan> findAllByRole(Role role);

    List<SeatBan> findById_SeatSectionAndRole(Character seatSection, Role role);
}
