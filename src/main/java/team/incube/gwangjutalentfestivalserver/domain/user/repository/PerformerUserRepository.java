package team.incube.gwangjutalentfestivalserver.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.PerformerUser;

public interface PerformerUserRepository extends JpaRepository<PerformerUser, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
