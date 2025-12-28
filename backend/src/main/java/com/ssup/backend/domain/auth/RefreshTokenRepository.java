package com.ssup.backend.domain.auth;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ssup.backend.infra.security.jwt.TokenInfo.REFRESH_TOKEN;
import static com.ssup.backend.infra.security.jwt.TokenInfo.REFRESH_TOKEN_VALIDATION_SECOND;
import static java.time.Duration.ofMillis;

@Repository
public class RefreshTokenRepository {

    private static final String KEY = REFRESH_TOKEN;
    private final RedisTemplate<String, String> refreshTokenRedisTemplate;
    private final HashOperations<String, String, String> hashOperations;

    public RefreshTokenRepository(RedisTemplate<String, String> refreshTokenRedisTemplate) {
        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
        this.hashOperations = refreshTokenRedisTemplate.opsForHash();
    }

    public Optional<String> findByUserId(Long userId) {
        return Optional.ofNullable(hashOperations.get(KEY, String.valueOf(userId)));
    }

    public String save(Long id, String refreshToken) {
        hashOperations.put(KEY, String.valueOf(id), refreshToken);
        refreshTokenRedisTemplate.expire(KEY, ofMillis(REFRESH_TOKEN_VALIDATION_SECOND));
        return refreshToken;
    }

    public void deleteById(Long id) {
        hashOperations.delete(KEY, String.valueOf(id));
    }
}
