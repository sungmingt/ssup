package com.ssup.backend.domain.user.recommend.event;

import com.ssup.backend.domain.user.recommend.UserProfileEmbeddingService;
import com.ssup.backend.domain.user.recommend.UserRecommendPrecomputeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEmbeddingEventListener {

    private final UserProfileEmbeddingService embeddingService;
    private final UserRecommendPrecomputeService userRecommendPrecomputeService;

//    @TransactionalEventListener(
//            phase = TransactionPhase.AFTER_COMMIT
//    )
    @EventListener
    @Async
    public void handle(UserProfileChangeEvent event) {
        log.info("### eventListener called");

        //embedding 생성
        embeddingService.rebuild(event.userId());
        //추천 친구 리스트 미리 계산
        userRecommendPrecomputeService.precompute(event.userId(), 10);
    }
}