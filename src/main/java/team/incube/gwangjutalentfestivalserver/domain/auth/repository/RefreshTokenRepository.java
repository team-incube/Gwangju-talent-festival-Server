package team.incube.gwangjutalentfestivalserver.domain.auth.repository;

import team.incube.gwangjutalentfestivalserver.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
    void deleteById(UUID id);
}
