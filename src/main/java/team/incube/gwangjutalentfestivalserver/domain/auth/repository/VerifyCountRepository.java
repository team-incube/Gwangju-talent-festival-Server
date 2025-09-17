package team.incube.gwangjutalentfestivalserver.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.VerifyCount;

import java.util.Optional;

public interface VerifyCountRepository extends JpaRepository<VerifyCount, String> {
	Optional<VerifyCount> findByPhoneNumber(String phoneNumber);
}
