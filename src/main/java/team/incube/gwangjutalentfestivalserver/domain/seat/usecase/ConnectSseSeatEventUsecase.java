package team.incube.gwangjutalentfestivalserver.domain.seat.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.global.sse.SseEmitterManager;
import team.incube.gwangjutalentfestivalserver.global.util.UserUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ConnectSseSeatEventUsecase {
    private final UserUtil userUtil;
    private final SseEmitterManager sseEmitterManager;

    public SseEmitter execute() {
        SseEmitter emitter = sseEmitterManager.addEmitter(userUtil.getUser().getPhoneNumber());

        try {
            emitter.send(SseEmitter.event().name("INIT").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
