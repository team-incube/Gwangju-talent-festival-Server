package team.incube.gwangjutalentfestivalserver.domain.judge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    Optional<Judgement> findByUserAndTeam(User user, Team team);

    List<Judgement> findAllByUser(User user);

    boolean existsByUserAndTeam(User user, Team team);

    @Query("""
        select j from Judgement j
        join fetch j.user u
        join fetch j.team t
        order by u.id asc, t.id asc
    """)
    List<Judgement> findAllWithUserAndTeam();
}
