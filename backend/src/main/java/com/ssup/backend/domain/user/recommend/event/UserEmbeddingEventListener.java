package com.ssup.backend.domain.user.recommend.event;

import com.ssup.backend.domain.user.recommend.UserProfileEmbeddingService;
import com.ssup.backend.domain.user.recommend.UserRecommendPrecomputeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEmbeddingEventListener {

    private final UserProfileEmbeddingService embeddingService;
    private final ApplicationEventPublisher publisher;

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
        //precompute 이벤트 발행 (순서 보장)
        publisher.publishEvent(new EmbeddingCreateEvent(event.userId()));
    }
}