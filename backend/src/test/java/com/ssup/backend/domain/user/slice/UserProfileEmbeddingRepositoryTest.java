package com.ssup.backend.domain.user.slice;

import com.ssup.backend.domain.user.recommend.OpenAiClient;
import com.ssup.backend.domain.user.recommend.UserProfileEmbedding;
import com.ssup.backend.domain.user.recommend.UserProfileEmbeddingRepository;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.profile.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserProfileEmbeddingRepositoryTest {

    @Autowired
    UserProfileEmbeddingRepository repository;

    @MockBean
    OpenAiClient openAiClient;

    @Mock
    RedisTemplate<String, UserProfileEmbedding> userProfileEmbeddingRedisTemplate;

    @Mock
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserRepository userRepository;

    @Test
    void saveAndFindUserEmbedding() {
        UserProfileEmbedding embedding =
                new UserProfileEmbedding(
                        1L,
                        List.of(0.1, 0.2, 0.3),
                        LocalDateTime.now()
                );

        repository.save(embedding);

        Optional<UserProfileEmbedding> result =
                repository.findByUserId(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getVector()).hasSize(3);
    }
}