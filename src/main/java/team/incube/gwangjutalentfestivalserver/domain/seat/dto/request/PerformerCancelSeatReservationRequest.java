package team.incube.gwangjutalentfestivalserver.domain.seat.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PerformerCancelSeatReservationRequest {
    @Pattern(
            regexp = "^[A-J]$",
            message = "좌석 섹션은 A부터 J까지 존재합니다."
    )
    private String seatSection;

    @Min(value = 1, message = "좌석 번호는 최소 1번부터 존재합니다.")
    @Max(value = 154, message = "좌석 번호는 최대 154번까지 존재합니다.")
    private Integer seatNumber;
}
