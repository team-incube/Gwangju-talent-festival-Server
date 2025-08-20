package team.incube.gwangjutalentfestivalserver.global.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VoteSseEmitterManager {

    private static final long SSE_TIMEOUT_MILLIS = 30 * 60 * 1000L; // 30분

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(Long teamId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MILLIS);
        emitters.computeIfAbsent(teamId, k -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(teamId, emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            removeEmitter(teamId, emitter);
        });
        emitter.onError(e -> {
            emitter.completeWithError(e);
            removeEmitter(teamId, emitter);
        });

        return emitter;
    }

    public void removeEmitter(Long teamId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(teamId);
        if (list != null) {
            list.remove(emitter);
            if (list.isEmpty()) emitters.remove(teamId);
        }
    }

    public List<SseEmitter> getEmitters(Long teamId) {
        return emitters.getOrDefault(teamId, Collections.emptyList());
    }

    public Collection<SseEmitter> getAllEmitters() {
        List<SseEmitter> all = new ArrayList<>();
        emitters.values().forEach(all::addAll);
        return all;
    }
}

