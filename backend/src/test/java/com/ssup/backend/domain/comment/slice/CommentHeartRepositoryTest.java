package com.ssup.backend.domain.comment.slice;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.heart.comment.CommentHeart;
import com.ssup.backend.domain.heart.comment.CommentHeartRepository;
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
class CommentHeartRepositoryTest {

    @Autowired
    CommentHeartRepository commentHeartRepository;

    @Autowired
    private TestEntityManager tem;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        this.em = tem.getEntityManager();
    }

    @DisplayName("존재하는 좋아요 조회 시도 - 성공")
    @Test
    void existsByCommentAndUser_success() {
        User user = UserJpaFixture.createUser(em);
        Post post = getPost(user);
        Comment comment = getComment(post, user);

        commentHeartRepository.save(new CommentHeart(comment, user));

        Assertions.assertThat(commentHeartRepository.existsByCommentIdAndUserId(comment.getId(), user.getId()))
                .isTrue();
    }

    //=== init ===

    private Comment getComment(Post post, User author) {
        Comment comment = Comment.builder()
                .content("content")
                .author(author)
                .post(post)
                .build();

        return tem.persist(comment);
    }

    private Post getPost(User author) {
        Post post = Post.builder()
                .title("title ")
                .content("content")
                .author(author)
                .build();

        return tem.persist(post);
    }
}
