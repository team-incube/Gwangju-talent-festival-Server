package team.incube.gwangjutalentfestivalserver.global.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JudgeSseEmitterManager {
    private static final long SSE_TIMEOUT_MILLIS = 60 * 60 * 1000L; // 60분

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String phoneNumber) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MILLIS);
        emitters.put(phoneNumber, emitter);

        emitter.onCompletion(() -> emitters.remove(phoneNumber));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(phoneNumber);
        });
        emitter.onError(e -> {
            emitter.completeWithError(e);
            emitters.remove(phoneNumber);
        });

        return emitter;
    }

    public Optional<SseEmitter> getEmitter(UUID userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    public Collection<SseEmitter> getAllEmitters() {
        return emitters.values();
    }
}
