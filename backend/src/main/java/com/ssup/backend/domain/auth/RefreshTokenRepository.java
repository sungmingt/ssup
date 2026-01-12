package com.ssup.backend.domain.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

import static com.ssup.backend.infra.security.jwt.TokenInfo.REFRESH_TOKEN_TTL_MILLISECONDS;

@Repository
@Slf4j
public class RefreshTokenRepository {

    private static final String KEY_PREFIX = "refreshToken:";
    private final StringRedisTemplate redisTemplate;

    public RefreshTokenRepository(StringRedisTemplate refreshTokenRedisTemplate) {
        this.redisTemplate = refreshTokenRedisTemplate;
    }

    public String getKey(Long userId, String sessionId) {
        return KEY_PREFIX + userId + ":" + sessionId;
    }

    public Optional<String> findByUserId(Long userId, String sessionId) {
        log.info("### findByUserId called");
        log.info("### finding userId={}", userId);
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(userId, sessionId)));
    }

    public void save(Long userId, String sessionId, String refreshToken) {
        redisTemplate.opsForValue().set(
                getKey(userId, sessionId),
                refreshToken,
                Duration.ofMillis(REFRESH_TOKEN_TTL_MILLISECONDS)
        );
    }

    public void deleteById(Long userId, String sessionId) {
        redisTemplate.delete(getKey(userId, sessionId));
    }
}
