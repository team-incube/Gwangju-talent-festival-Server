package team.incube.gwangjutalentfestivalserver.domain.judge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.judge.entity.Judgement;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface JudgementRepository extends JpaRepository<Judgement, Long> {
    Optional<Judgement> findByUserAndTeam(User user, Team team);

    List<Judgement> findAllByUser(User user);

    boolean existsByUserAndTeam(User user, Team team);
}
