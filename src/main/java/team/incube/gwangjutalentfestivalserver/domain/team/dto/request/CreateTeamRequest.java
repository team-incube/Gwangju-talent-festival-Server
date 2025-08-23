package team.incube.gwangjutalentfestivalserver.domain.team.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamRequest {
    @NotNull
    private String teamName;
}
