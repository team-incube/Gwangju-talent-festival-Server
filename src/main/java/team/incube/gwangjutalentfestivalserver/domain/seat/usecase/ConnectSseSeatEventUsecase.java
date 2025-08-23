package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.global.sse.SeatSseEmitterManager;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

@Service
@RequiredArgsConstructor
public class ConnectSseSeatEventUsecase {
    private final UserUtil userUtil;
    private final SeatSseEmitterManager sseEmitterManager;

    public SseEmitter execute() {
        return sseEmitterManager.addEmitter(userUtil.getUser().getId());
    }
}
