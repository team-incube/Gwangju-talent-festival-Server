package team.incube.gwangjutalentfestivalserver.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.Vote;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByTeamId(Long teamId);

    @Query("SELECT MAX(v.id.id) FROM Vote v WHERE v.id.teamId = :teamId")
    Optional<Long> findMaxIdByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT v.id.id FROM Vote v WHERE v.id.teamId = :teamId AND v.id.id = :voteId")
    Optional<Long> findVoteId(@Param("teamId") Long teamId, @Param("voteId") Long voteId);
}
