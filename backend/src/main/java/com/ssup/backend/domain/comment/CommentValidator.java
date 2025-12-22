package com.ssup.backend.domain.comment;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import org.springframework.stereotype.Component;

import static com.ssup.backend.global.exception.ErrorCode.*;
import static com.ssup.backend.global.exception.ErrorCode.COMMENT_NOT_AUTHORIZED;

@Component
public class CommentValidator {

    public void validateOwner(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new SsupException(COMMENT_NOT_AUTHORIZED);
        }
    }

    public void validateResource(Comment comment, Long postId) {
        if (comment.isDeleted() || !comment.getPost().getId().equals(postId)) {
            throw new SsupException(COMMENT_NOT_FOUND);
        }
    }

    public void validateComment(Comment comment, Long postId, Long userId) {
        validateOwner(comment, userId);
        validateResource(comment, postId);
    }
}