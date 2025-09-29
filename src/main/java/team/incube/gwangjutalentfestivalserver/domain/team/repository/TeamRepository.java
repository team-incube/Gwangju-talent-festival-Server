package team.incube.gwangjutalentfestivalserver.domain.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.incube.gwangjutalentfestivalserver.domain.team.dto.TeamRankingDto;
import team.incube.gwangjutalentfestivalserver.domain.team.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByEventYear(Integer eventYear);

    @Query(value = """
        select
            row_number() over (
                order by
                    t.total_score desc,
                    coalesce(j.avg_completion, 0) desc,
                    coalesce(j.avg_creativity, 0) desc,
                    coalesce(j.avg_stage, 0) desc,
                    t.star desc,
                    t.id asc
            ) as ranking,
            t.team_name as team_name
        from team t
        left join (
            select
                team_id,
                avg(completion_expression)      as avg_completion,
                avg(creativity_composition)     as avg_creativity,
                avg(stage_manner_performance)   as avg_stage
            from judgement
            group by team_id
        ) j on j.team_id = t.id
        where t.event_year = :eventYear
        order by ranking asc
        """, nativeQuery = true)
    List<TeamRankingDto> findAllByEventYearWithRanking(@Param("eventYear") int eventYear);

    @Query(value = """
        with ranked_by_score as (
            select
                t.id,
                t.team_name,
                t.star,
                row_number() over (
                    order by t.total_score desc, t.id asc
                ) as score_rank
            from team t
            where t.event_year = :eventYear
        )
        select r.team_name
        from ranked_by_score r
        where r.score_rank > 3
        order by r.star desc nulls last, r.id asc
        limit 2
        """, nativeQuery = true)
    List<String> findTop2TeamsByStar(@Param("eventYear") int eventYear);

    List<Team> findAllByOrderByIdAsc();
}
