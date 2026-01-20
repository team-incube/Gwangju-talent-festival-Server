package team.incube.gwangjutalentfestivalserver.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.VoteUser;

import java.util.UUID;

public interface VoteUserRepository extends JpaRepository<VoteUser, Long> {
    boolean existsByTeamIdAndUserId(Long teamId, UUID userId);
}