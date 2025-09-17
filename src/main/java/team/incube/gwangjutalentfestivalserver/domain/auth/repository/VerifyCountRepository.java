package team.incube.gwangjutalentfestivalserver.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.VerifyCount;

import java.util.Optional;

public interface VerifyCountRepository extends CrudRepository<VerifyCount, String> {
	Optional<VerifyCount> findByPhoneNumber(String phoneNumber);
}
