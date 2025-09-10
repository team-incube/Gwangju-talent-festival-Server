package team.incube.gwangjutalentfestivalserver.domain.judge.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SaveJudgementRequest {
    @NotNull
    @Min(value = 0, message = "심사 점수는 마이너스로 줄 수 없습니다.")
    @Max(value = 40, message = "완성도 및 표현력 부문은 최대 40점입니다.")
    private Integer completionExpression;

    @NotNull
    @Min(value = 0, message = "심사 점수는 마이너스로 줄 수 없습니다.")
    @Max(value = 30, message = "창의력과 구성 부문은 최대 30점입니다.")
    private Integer creativityComposition;

    @NotNull
    @Min(value = 0, message = "심사 점수는 마이너스로 줄 수 없습니다.")
    @Max(value = 30, message = "무대 매너 및 퍼포먼스 부문은 최대 30점입니다.")
    private Integer stageMannerPerformance;
}
