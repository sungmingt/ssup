package com.ssup.backend.domain.comment.slice;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.comment.CommentRepository;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("특정 글의 댓글목록 조회 - 삭제되지 않은 댓글만 조회")
    @Test
    void findList_onlyUndeletedComments() {
        //given
        User user = getUser();
        Post post = getPost(10, user);
        getComment(post, user, "comment1", false);
        getComment(post, user, "comment2", true);

        //when
        List<Comment> result =
                commentRepository.findByPostIdAndDeletedFalseOrderByCreatedAtAsc(post.getId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("comment1");
    }

    //=== init ===

    private Comment getComment(Post post, User user, String content, boolean deleted) {
        Comment comment = em.persist(Comment.builder()
                .post(post)
                .author(user)
                .content(content)
                .deleted(deleted)
                .build());

        em.flush();
        em.clear();
        return comment;
    }

    private Post getPost(int viewCount, User author) {
        Post post = em.persist(Post.builder()
                .title("title ")
                .content("content")
                .author(author)
                .viewCount(viewCount)
                .build());

        em.flush();
        em.clear();
        return post;
    }

    private User getUser() {
        User user = em.persist(User.builder()
                .email("email123@gmail.com")
                .build());

        em.flush();
        em.clear();
        return user;
    }
}