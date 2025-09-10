package team.incube.gwangjutalentfestivalserver.domain.judge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetJudgementResponse {
    private Long judgeId;
    private Long teamId;
    private String teamName;
    private Integer completionExpression;
    private Integer creativityComposition;
    private Integer stageMannerPerformance;
    private Integer totalScore;
    private Boolean isPerformed;
    private Boolean isJudged;
}

