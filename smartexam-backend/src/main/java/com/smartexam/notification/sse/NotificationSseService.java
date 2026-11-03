package com.smartexam.notification.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class NotificationSseService {

    private static final long SSE_TIMEOUT =
            30 * 60 * 1000L; // 30 minutes

    private final Map<Long, SseEmitter> emitters =
            new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {

        SseEmitter emitter =
                new SseEmitter(SSE_TIMEOUT);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(userId);
            log.info(
                    "SSE connection completed for userId={}",
                    userId
            );
        });

        emitter.onTimeout(() -> {
            emitters.remove(userId);
            log.info(
                    "SSE connection timed out for userId={}",
                    userId
            );
            emitter.complete();
        });

        emitter.onError(error -> {
            emitters.remove(userId);
            log.warn(
                    "SSE connection error for userId={}",
                    userId,
                    error
            );
        });

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("CONNECTED")
                            .data("Notification connection established")
            );
        } catch (IOException e) {
            emitters.remove(userId);
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void sendToUser(
            Long userId,
            Object notification) {

        SseEmitter emitter =
                emitters.get(userId);

        if (emitter == null) {
            return;
        }

        try {

            emitter.send(
                    SseEmitter.event()
                            .name("NOTIFICATION")
                            .data(notification)
            );

        } catch (IOException e) {

            emitters.remove(userId);
            emitter.completeWithError(e);

            log.warn(
                    "Unable to send notification to userId={}",
                    userId,
                    e
            );
        }
    }
}