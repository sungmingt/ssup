package com.ssup.backend.domain.comment;

import com.ssup.backend.global.exception.SsupException;
import org.springframework.stereotype.Component;

import static com.ssup.backend.global.exception.ErrorCode.NOT_COMMENT_OWNER;

@Component
public class CommentValidator {

    private CommentValidator(){}

    public static void validateOwner(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new SsupException(NOT_COMMENT_OWNER);
        }
    }

    public static void validateResource(Comment comment, Long postId) {
        if (comment.isDeleted() || !comment.getPost().getId().equals(postId)) {
            throw new SsupException(NOT_COMMENT_OWNER);
        }
    }

    public static void validateComment(Comment comment, Long postId, Long userId) {
        validateOwner(comment, userId);
        validateResource(comment, postId);
    }
}