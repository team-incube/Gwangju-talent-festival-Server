package team.incube.gwangjutalentfestivalserver.domain.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.vote.entity.VoteUser;

public interface VoteUserRepository extends JpaRepository<VoteUser, Long> {
    boolean existsByTeamAndUser(Team team, User user);
}
