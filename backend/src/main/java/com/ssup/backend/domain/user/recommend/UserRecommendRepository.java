package com.ssup.backend.domain.user.recommend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRecommendRepository {

    private static final String KEY_PREFIX = "user:recommend:";

    @Qualifier("recommendRedisTemplate")
    private final RedisTemplate<String, List<Long>> recommendRedisTemplate;

    public UserRecommendRepository(RedisTemplate<String, List<Long>> recommendRedisTemplate) {
        this.recommendRedisTemplate = recommendRedisTemplate;
    }

    public void save(Long userId, List<Long> recommendedUserIds) {
        recommendRedisTemplate.opsForValue().set(
                KEY_PREFIX + userId, recommendedUserIds
        );
    }

    public List<Long> find(Long userId) {
        List<?> result = recommendRedisTemplate.opsForValue().get(KEY_PREFIX + userId);
        if (result == null) return List.of();
        return result.stream()
                .map(v -> ((Number) v).longValue())
                .toList();
    }


    public void delete(Long userId) {
        recommendRedisTemplate.delete(KEY_PREFIX + userId);
    }
}