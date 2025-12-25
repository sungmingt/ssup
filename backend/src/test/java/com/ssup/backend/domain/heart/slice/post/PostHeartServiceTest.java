package com.ssup.backend.domain.heart.slice.post;

import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.domain.heart.post.PostHeartService;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.fixture.user.UserJpaFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PostHeartServiceTest {

    @Autowired
    PostHeartService postHeartService;

    @Autowired
    EntityManager em;

    @DisplayName("좋아요 최초 시도 - 성공")
    @Test
    void findHearts_firstTime_success() {
        //given
        User user = UserJpaFixture.createUser(em);
        Post post = getPost(10, user);

        //when
        HeartResponse response =
                postHeartService.toggleHeart(post.getId(), user.getId());

        //then
        assertThat(response.isHearted()).isTrue();
        assertThat(response.getHeartCount()).isEqualTo(1);
    }

    @DisplayName("좋아요 취소 - 성공")
    @Test
    void undoHeart_success() {
        //given
        User user = UserJpaFixture.createUser(em);
        Post post = getPost(10, user);

        //when
        postHeartService.toggleHeart(post.getId(), user.getId());
        HeartResponse response =
                postHeartService.toggleHeart(post.getId(), user.getId());

        //then
        assertThat(response.isHearted()).isFalse();
        assertThat(response.getHeartCount()).isZero();
    }

    private Post getPost(int viewCount, User author) {
        Post post = Post.builder()
                .title("title ")
                .content("content")
                .author(author)
                .viewCount(viewCount)
                .build();

        em.persist(post);
        return post;
    }
}