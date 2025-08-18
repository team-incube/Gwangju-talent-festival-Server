package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.global.sse.VoteSseEmitterManager;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ConnectSseVoteEventUsecase {

    private final VoteSseEmitterManager sseEmitterManager;

    public SseEmitter execute(Long teamId) {
        SseEmitter emitter = sseEmitterManager.addEmitter(teamId);

        try {
            emitter.send(SseEmitter.event().name("INIT").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
