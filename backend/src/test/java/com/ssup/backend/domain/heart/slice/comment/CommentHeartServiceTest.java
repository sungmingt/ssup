package com.ssup.backend.domain.heart.slice.comment;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.heart.comment.CommentHeartService;
import com.ssup.backend.domain.heart.dto.HeartResponse;
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
class CommentHeartServiceTest {

    @Autowired
    CommentHeartService commentHeartService;

    @Autowired
    EntityManager em;

    @DisplayName("좋아요 최초 시도 - 성공")
    @Test
    void findHearts_firstTime_success() {
        //given
        User user = UserJpaFixture.createUser(em);
        Post post = getPost(user);
        Comment comment = getComment(user, post);

        //when
        HeartResponse response =
                commentHeartService.toggleHeart(comment.getId(), user.getId());

        //then
        assertThat(response.isHearted()).isTrue();
        assertThat(response.getHeartCount()).isEqualTo(1);
    }

    @DisplayName("좋아요 취소 - 성공")
    @Test
    void undoHeart_success() {
        //given
        User user = UserJpaFixture.createUser(em);
        Post post = getPost(user);
        Comment comment = getComment(user, post);

        //when
        commentHeartService.toggleHeart(comment.getId(), user.getId());
        HeartResponse response =
                commentHeartService.toggleHeart(comment.getId(), user.getId());

        //then
        assertThat(response.isHearted()).isFalse();
        assertThat(response.getHeartCount()).isZero();
    }

    private Comment getComment(User author, Post post) {
        Comment comment = Comment.builder()
                .content("content")
                .author(author)
                .post(post)
                .build();

        em.persist(comment);
        return comment;
    }

    private Post getPost(User author) {
        Post post = Post.builder()
                .title("title ")
                .content("content")
                .author(author)
                .build();

        em.persist(post);
        return post;
    }
}