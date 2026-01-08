package com.ssup.backend.domain.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CommentTest {

    @Test
    @DisplayName("댓글 내용 수정 테스트")
    void updateContent() {
        Comment comment = Comment.builder()
                .content("old")
                .deleted(false)
                .build();

        comment.updateContent("new");
        assertThat(comment.getContent()).isEqualTo("new");
    }

    @Test
    @DisplayName("댓글 이미지 수정 테스트")
    void updateImageUrl() {
        Comment comment = Comment.builder()
                .imageUrl("old.png")
                .content("hi")
                .deleted(false)
                .build();

        comment.updateImageUrl("new.png");
        assertThat(comment.getImageUrl()).isEqualTo("new.png");
    }

    @Test
    @DisplayName("댓글 soft delete 테스트")
    void softDelete() {
        Comment comment = Comment.builder()
                .content("hi")
                .deleted(false)
                .build();

        comment.softDelete();
        assertThat(comment.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("댓글 좋아요 수 증가 테스트")
    void increaseHeartCount() {
        Comment comment = Comment.builder()
                .heartCount(0)
                .content("hi")
                .deleted(false)
                .build();

        comment.increaseHeartCount();
        assertThat(comment.getHeartCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 좋아요 수 감소 테스트")
    void decreaseHeartCount() {
        Comment comment = Comment.builder()
                .heartCount(2)
                .content("hi")
                .deleted(false)
                .build();

        comment.decreaseHeartCount();
        assertThat(comment.getHeartCount()).isEqualTo(1);
    }
}