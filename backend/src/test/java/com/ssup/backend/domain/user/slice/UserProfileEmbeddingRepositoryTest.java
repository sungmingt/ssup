package com.ssup.backend.domain.user.slice;

import com.ssup.backend.domain.user.recommend.UserProfileEmbedding;
import com.ssup.backend.domain.user.recommend.UserProfileEmbeddingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @DisplayName("유저 프로필 embedding 저장 및 조회가 성공적으로 수행된다.")
    @Test
    void saveAndFindUserEmbedding() {
        UserProfileEmbedding embedding =
                new UserProfileEmbedding(
                        1L,
                        List.of(0.1, 0.2, 0.3),
                        LocalDateTime.now()
                );

        repository.save(embedding);

        Optional<UserProfileEmbedding> result = repository.findByUserId(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getVector()).hasSize(3);
    }
}