package com.ssup.backend.domain.user.recommend.event;

import com.ssup.backend.domain.user.recommend.UserRecommendPrecomputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrecomputeEventListener {

    private final UserRecommendPrecomputeService precomputeService;

    @Async
    @EventListener
    public void handle(EmbeddingCreateEvent event) {
        precomputeService.precompute();
    }
}