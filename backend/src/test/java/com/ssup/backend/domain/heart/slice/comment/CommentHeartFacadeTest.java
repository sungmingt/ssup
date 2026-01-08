package com.ssup.backend.domain.heart.slice.comment;

import com.ssup.backend.domain.heart.comment.CommentHeartFacade;
import com.ssup.backend.domain.heart.comment.CommentHeartService;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentHeartFacadeTest {

    @InjectMocks
    private CommentHeartFacade commentHeartFacade;

    @Mock
    private CommentHeartService commentHeartService;

    @Test
    @DisplayName("좋아요 첫 시도 - 성공")
    void tryToggle_success_first() {
        //given
        HeartResponse response = new HeartResponse(true, 10);
        given(commentHeartService.toggleHeart(1L, 2L))
                .willReturn(response);

        //when
        HeartResponse result = commentHeartFacade.tryToggleHeart(1L, 2L);

        //then
        assertThat(result).isEqualTo(response);
        verify(commentHeartService, times(1)).toggleHeart(1L, 2L);
    }

    @Test
    @DisplayName("1회 OptimisticLockException 후 성공")
    void tryToggle_retry_once() {
        //given
        HeartResponse response = new HeartResponse(true, 10);
        given(commentHeartService.toggleHeart(1L, 2L))
                .willThrow(new OptimisticLockException())
                .willReturn(response);

        //when
        HeartResponse result = commentHeartFacade.tryToggleHeart(1L, 2L);

        //then
        assertThat(result).isEqualTo(response);
        verify(commentHeartService, times(2)).toggleHeart(1L, 2L);
    }

    @Test
    @DisplayName("좋아요 3회 실패 시 예외가 발생한다")
    void tryToggle_retry_fail() {
        //given
        given(commentHeartService.toggleHeart(1L, 2L))
                .willThrow(new OptimisticLockException());

        //when, then
        assertThatThrownBy(() -> commentHeartFacade.tryToggleHeart(1L, 2L))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.TOO_MANY_TRAFFIC.getMessage());

        verify(commentHeartService, times(3)).toggleHeart(1L, 2L);
    }
}