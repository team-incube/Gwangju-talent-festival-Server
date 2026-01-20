package team.incube.gwangjutalentfestivalserver.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.Vote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByTeamId(Long teamId);

    boolean existsByTeamIdAndUserId(Long teamId, UUID userId);

    Optional<Vote> findByTeamIdAndUserId(Long teamId, UUID userId);
}