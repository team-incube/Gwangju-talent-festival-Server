package team.incube.gwangjutalentfestivalserver.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.TeamMembership;

import java.util.List;

public interface TeamMembershipRepository extends JpaRepository<TeamMembership, Long> {
    List<TeamMembership> findAllByTeamId(Long teamId);
}
