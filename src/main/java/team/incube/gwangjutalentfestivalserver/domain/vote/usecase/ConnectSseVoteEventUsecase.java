package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.incube.gwangjutalentfestivalserver.global.sse.VoteSseEmitterManager;

@Service
@RequiredArgsConstructor
public class ConnectSseVoteEventUsecase {

    private final VoteSseEmitterManager sseEmitterManager;

    public SseEmitter execute(Long teamId) {
        return sseEmitterManager.addEmitter(teamId);
    }
}
