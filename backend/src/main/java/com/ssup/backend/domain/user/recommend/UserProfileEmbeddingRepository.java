package com.ssup.backend.domain.user.recommend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserProfileEmbeddingRepository {

    //embedding 요청의 결과를 redis에 저장

    private static final String KEY_PREFIX = "user:embedding:";
    private static final String INDEX_KEY = "embedding:users";

    @Qualifier("embeddingRedisTemplate")
    private final RedisTemplate<String, UserProfileEmbedding> embeddingRedisTemplate;
    private final StringRedisTemplate keysRedisTemplate;

    public UserProfileEmbeddingRepository(
            RedisTemplate<String, UserProfileEmbedding> embeddingRedisTemplate,
            StringRedisTemplate keysRedisTemplate) {

        this.embeddingRedisTemplate = embeddingRedisTemplate;
        this.keysRedisTemplate = keysRedisTemplate;
    }

    public void save(UserProfileEmbedding embedding) {
        String key = generateKey(embedding.getUserId());
        embeddingRedisTemplate.opsForValue().set(key, embedding);
        keysRedisTemplate.opsForSet().add(INDEX_KEY,
                embedding.getUserId().toString());
    }

    public Optional<UserProfileEmbedding> findByUserId(Long userId) {
        String key = generateKey(userId);
        return Optional.ofNullable(embeddingRedisTemplate.opsForValue().get(key));
    }

    public List<UserProfileEmbedding> findAllEmbeddings() {
        Set<String> userIds = keysRedisTemplate.opsForSet().members(INDEX_KEY);

        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        //key 목록 생성
        List<String> keys = userIds.stream()
                .map(id -> KEY_PREFIX + id)
                .toList();

        //multiGet -> 한 번에 조회
        List<UserProfileEmbedding> embeddings =
                embeddingRedisTemplate.opsForValue().multiGet(keys);

        return embeddings.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    public void deleteByUserId(Long userId) {
        embeddingRedisTemplate.delete(generateKey(userId));
        keysRedisTemplate.opsForSet().remove(INDEX_KEY, userId.toString());
    }


    private String generateKey(Long userId) {
        return KEY_PREFIX + userId;
    }
}
