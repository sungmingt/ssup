package com.ssup.backend.domain.heart.comment;

import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentHeartFacade {

    private final CommentHeartService commentHeartService;

    //동시 요청으로 인한 Lost Update 방지를 위해 낙관적 락 retry 필요한데,
    // 같은 service 클래스에서 호출할 경우 self-invocation 으로 인해 트랜잭션이 분리되지 않음.
    // -> 책임 분리
    public HeartResponse tryToggleHeart(Long userId, Long commentId) {
        for (int i = 0; i < 3; i++) {
            try {
                return commentHeartService.toggleHeart(commentId, userId);
            } catch (OptimisticLockException e) {
                log.debug(
                        "### [좋아요 재시도] commentId={}, userId={}, attempt={}",
                        commentId, userId, i + 1
                );
            }
        }

        throw new SsupException(ErrorCode.TOO_MANY_TRAFFIC);
    }
}