package com.ssup.backend.domain.user.recommend.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProfileChangeTrigger {

    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher publisher;
    private final TaskScheduler taskScheduler;

    private static final long DEBOUNCE_SECONDS = 5;
    private static final String KEY_PREFIX = "user:profile:change:";

    public void trigger(Long userId) {
        log.info("### trigger called, userId={}", userId);

        String key = buildKey(userId);

        Boolean isFirst = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofSeconds(DEBOUNCE_SECONDS));

        log.info("### isFirst={}", isFirst);

        if (Boolean.TRUE.equals(isFirst)) {
            //최초 1회만 지연 실행 예약
            scheduleEvent(userId);
            log.info("### scheduleEvent called, userId={}", userId);
        }
    }

    private void scheduleEvent(Long userId) {
        taskScheduler.schedule(
                () -> publisher.publishEvent(new UserProfileChangeEvent(userId)),
                Instant.now().plusSeconds(DEBOUNCE_SECONDS)
        );
    }

    private String buildKey(Long userId) {
        return KEY_PREFIX + userId;
    }
}