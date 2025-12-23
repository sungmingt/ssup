package com.ssup.backend.domain.heart.comment;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.comment.CommentRepository;
import com.ssup.backend.domain.heart.HeartService;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentHeartService implements HeartService {

    private final CommentRepository commentRepository;
    private final CommentHeartRepository heartRepository;
    private final UserRepository userRepository;

    /**
     * 실제 좋아요 수행
     * - 항상 새로운 트랜잭션에서 실행
     * - 낙관적 락(@Version)은 Comment 엔티티에서 동작
     */
    @Override
    public HeartResponse toggleHeart(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SsupException(ErrorCode.COMMENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(ErrorCode.USER_NOT_FOUND));

        //좋아요를 누른 상태 → 취소
        if (heartRepository.existsByCommentIdAndUserId(commentId, userId)) {
            heartRepository.deleteByCommentAndUser(comment, user);
            comment.decreaseHeartCount();
            return HeartResponse.of(false, comment.getHeartCount());
        }

        //좋아요
        try {
            heartRepository.save(new CommentHeart(comment, user));
            comment.increaseHeartCount();
            return HeartResponse.of(true, comment.getHeartCount());
        } catch (DataIntegrityViolationException e) {
            //UNIQUE(user_id, comment_id) 충돌
            throw new SsupException(ErrorCode.HEART_ALREADY_EXISTS);
        }
    }

    public Set<Long> findHeartedCommentIds(Long userId, List<Comment> comments) {
        Set<Long> heartedCommentIds = Collections.emptySet();

        if (userId != null && !comments.isEmpty()) {
            List<Long> commentIds = comments.stream()
                    .map(Comment::getId)
                    .toList();

            heartedCommentIds = new HashSet<>(
                    heartRepository.findHeartedCommentIds(userId, commentIds)
            );
        }

        return heartedCommentIds;
    }
}
