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

    private String getKey(Long userId) {
        return KEY_PREFIX + userId;
    }

    public Optional<String> findByUserId(Long userId) {
        log.info("### findByUserId called");
        log.info("### finding userId={}", userId);
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(userId)));
    }

    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                getKey(userId),
                refreshToken,
                Duration.ofSeconds(REFRESH_TOKEN_TTL_MILLISECONDS)
        );
    }

    public void deleteById(Long userId) {
        redisTemplate.delete(getKey(userId));
    }
}
