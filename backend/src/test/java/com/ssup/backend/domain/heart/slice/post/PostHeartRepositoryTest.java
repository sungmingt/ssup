package com.ssup.backend.domain.heart.slice.post;

import com.ssup.backend.domain.heart.post.PostHeart;
import com.ssup.backend.domain.heart.post.PostHeartRepository;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.fixture.user.UserJpaFixture;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class PostHeartRepositoryTest {

    @Autowired
    PostHeartRepository postHeartRepository;

    @Autowired
    private TestEntityManager tem;
    private EntityManager em;

    private User author;

    @DisplayName("존재하는 좋아요 조회 시도 - 성공")
    @Test
    void existsByPostAndUser_success() {
        //given
        Post post = getPost(1L, 10, author);
        postHeartRepository.save(new PostHeart(post, author));

        //when, then
        Assertions.assertThat(postHeartRepository.existsByPostIdAndUserId(post.getId(), author.getId()))
                .isTrue();
    }

    //=== init ===

    @BeforeEach
    void setUp() {
        this.em = tem.getEntityManager();

        author = UserJpaFixture.createUser(em);

        //viewCount 기준 정렬 테스트용
        getPost(1, 300, author);
        getPost(2, 300, author);
        getPost(3, 250, author);
        getPost(4, 250, author);
        getPost(5, 200, author);
        getPost(6, 200, author);
        getPost(7, 150, author);
    }

    private Post getPost(long id, int viewCount, User author) {
        Post post = Post.builder()
                .title("title " + id)
                .content("content")
                .author(author)
                .viewCount(viewCount)
                .build();

        return tem.persist(post);
    }
}
