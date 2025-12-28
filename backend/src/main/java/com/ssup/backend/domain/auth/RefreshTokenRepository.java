package com.ssup.backend.domain.auth;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

import static com.ssup.backend.infra.security.jwt.TokenInfo.REFRESH_TOKEN_VALIDATION_SECOND;

@Repository
public class RefreshTokenRepository {

    private static final String KEY_PREFIX = "refreshToken:";
    private final RedisTemplate<String, String> refreshTokenRedisTemplate;
    private final ValueOperations<String, String> ops;

    public RefreshTokenRepository(RedisTemplate<String, String> refreshTokenRedisTemplate) {
        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
        ops = refreshTokenRedisTemplate.opsForValue();
    }

    private String getKey(Long userId) {
        return KEY_PREFIX + userId;
    }

    public Optional<String> findByUserId(Long userId) {
        return Optional.ofNullable(ops.get(getKey(userId)));
    }

    public void save(Long userId, String refreshToken) {
        ops.set(
                getKey(userId),
                refreshToken,
                Duration.ofMillis(REFRESH_TOKEN_VALIDATION_SECOND)
        );
    }

    public void deleteById(Long userId) {
        refreshTokenRedisTemplate.delete(getKey(userId));
    }
}
