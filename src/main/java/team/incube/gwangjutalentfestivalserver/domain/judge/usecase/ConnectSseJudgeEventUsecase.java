package team.incube.gwangjutalentfestivalserver.domain.judge.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.global.sse.JudgeSseEmitterManager;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ConnectSseJudgeEventUsecase {
    private final JudgeSseEmitterManager sseEmitterManager;

    public SseEmitter execute() {
        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        SseEmitter emitter = sseEmitterManager.addEmitter(phoneNumber);

        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .id(String.valueOf(System.currentTimeMillis()))
                    .data("ok"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            return emitter;
        }
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        var beat = scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("heartbeat")
                        .id(String.valueOf(System.currentTimeMillis()))
                        .data("ok"));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 15, 15, TimeUnit.SECONDS);

        emitter.onCompletion(() -> { beat.cancel(true); scheduler.shutdown(); });
        emitter.onTimeout(() -> { beat.cancel(true); scheduler.shutdown(); });
        emitter.onError(e -> { beat.cancel(true); scheduler.shutdown(); });

        return emitter;
    }
}
