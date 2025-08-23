package team.incube.gwangjutalentfestivalserver.domain.judge.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.domain.judge.event.JudgementTeamEvent;
import team.incube.gwangjutalentfestivalserver.global.sse.JudgeSseEmitterManager;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JudgementTeamEventListener {
    private final JudgeSseEmitterManager sseEmitterManager;

    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void execute(JudgementTeamEvent data) {
        for (SseEmitter emitter : sseEmitterManager.getAllEmitters()) {
            try {
                emitter.send(SseEmitter.event().name("PERFORM_TEAM_CHANGE").data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
