package com.ssup.backend.domain.heart.slice.post;

import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.domain.heart.post.PostHeartFacade;
import com.ssup.backend.domain.heart.post.PostHeartService;
import com.ssup.backend.global.exception.SsupException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostHeartFacadeTest {

    @InjectMocks
    PostHeartFacade facade;

    @Mock
    PostHeartService postHeartService;

    @DisplayName("동일 리소스 동시 좋아요 요청 - 재시도가 수행")
    @Test
    void retryMustBeCalled_whenOptimisticLockException() {
        //given
        given(postHeartService.toggleHeart(1L, 1L))
                .willThrow(OptimisticLockException.class)
                .willReturn(HeartResponse.of(true, 1L));

        //when
        HeartResponse response = facade.tryToggleHeart(1L, 1L);

        //then
        assertThat(response.isHearted()).isTrue();
        verify(postHeartService, times(2))
                .toggleHeart(1L, 1L);
    }

    @DisplayName("재시도 횟수 초과 - 예외를 던진다.")
    @Test
    void throwException_whenRetryOverCalled() {
        //given
        given(postHeartService.toggleHeart(any(), any()))
                .willThrow(OptimisticLockException.class);

        //when, then
        assertThatThrownBy(() -> facade.tryToggleHeart(1L, 1L))
                .isInstanceOf(SsupException.class);
    }
}