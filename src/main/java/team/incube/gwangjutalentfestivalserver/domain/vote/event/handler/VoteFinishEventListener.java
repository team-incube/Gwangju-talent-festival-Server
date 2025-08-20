package team.incube.gwangjutalentfestivalserver.domain.vote.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.domain.vote.event.VoteFinishEvent;
import team.incube.gwangjutalentfestivalserver.global.sse.VoteSseEmitterManager;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VoteFinishEventListener {

    private final VoteSseEmitterManager sseEmitterManager;

    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVoteFinish(VoteFinishEvent event) {
        for (SseEmitter emitter : sseEmitterManager.getEmitters(event.getTeamId())) {
            try {
                emitter.send(SseEmitter.event()
                        .name("VOTE_FINISH")
                        .data(event));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            } finally {
                sseEmitterManager.removeEmitter(event.getTeamId(), emitter);
            }
        }
    }
}

