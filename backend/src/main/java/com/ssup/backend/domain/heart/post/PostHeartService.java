package com.ssup.backend.domain.heart.post;

import com.ssup.backend.domain.heart.HeartService;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.aop.CheckUserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostHeartService implements HeartService {

    private final PostRepository postRepository;
    private final PostHeartRepository heartRepository;
    private final UserRepository userRepository;

    /**
     * 실제 좋아요 수행
     * - 항상 새로운 트랜잭션에서 실행
     * - 낙관적 락(@Version)은 Post 엔티티에서 동작
     */
    @CheckUserStatus(UserStatus.ACTIVE)
    @Override
    public HeartResponse toggleHeart(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SsupException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(ErrorCode.USER_NOT_FOUND));

        //좋아요를 누른 상태 → 취소
        if (heartRepository.existsByPostIdAndUserId(postId, userId)) {
            heartRepository.deleteByPostAndUser(post, user);
            post.decreaseHeartCount();
            return HeartResponse.of(false, post.getHeartCount());
        }

        //좋아요
        try {
            heartRepository.save(new PostHeart(post, user));
            post.increaseHeartCount();
            return HeartResponse.of(true, post.getHeartCount());
        } catch (DataIntegrityViolationException e) {
            //UNIQUE(user_id, post_id) 충돌
            throw new SsupException(ErrorCode.HEART_ALREADY_EXISTS);
        }
    }
}
