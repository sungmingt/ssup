package com.ssup.backend.domain.post;

import com.ssup.backend.global.exception.SsupException;
import org.springframework.stereotype.Component;

import static com.ssup.backend.global.exception.ErrorCode.NOT_POST_OWNER;

@Component
public class PostValidator {

    private PostValidator(){}

    public static void validatePostOwner(Long currentUserId, Long authorId) {
        if (!currentUserId.equals(authorId)) {
            throw new SsupException(NOT_POST_OWNER);
        }
    }
}