package com.ssup.backend.domain.user.slice;

import com.ssup.backend.domain.user.profile.dto.UserLocationResponse;
import com.ssup.backend.domain.user.recommend.UserProfileQueryRepository;
import com.ssup.backend.domain.user.recommend.UserRecommendRepository;
import com.ssup.backend.domain.user.recommend.UserRecommendService;
import com.ssup.backend.domain.user.recommend.dto.UserRecommendResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRecommendServiceTest {

    @InjectMocks
    private UserRecommendService userRecommendService;

    @Mock
    private UserRecommendRepository recommendationRepository;

    @Mock
    private UserProfileQueryRepository userProfileQueryRepository;

    @DisplayName("추천 유저 조회 - 유저 순서가 유지된다.")
    @Test
    void recommend_success_order_preserved() {
        //given
        Long userId = 1L;

        List<Long> ids = List.of(3L, 1L, 2L);

        when(recommendationRepository.find(userId))
                .thenReturn(ids);

        List<UserRecommendResponse> queryResult = List.of(
                createDto(1L, "user1"),
                createDto(2L, "user2"),
                createDto(3L, "user3")
        );

        when(userProfileQueryRepository.findRecommendUsers(ids))
                .thenReturn(queryResult);

        //when
        List<UserRecommendResponse> result =
                userRecommendService.recommend(userId);

        //then
        assertThat(result).hasSize(3);

        //추천 순서 유지
        assertThat(result.get(0).getId()).isEqualTo(3L);
        assertThat(result.get(1).getId()).isEqualTo(1L);
        assertThat(result.get(2).getId()).isEqualTo(2L);
    }

    @DisplayName("일부 유저 누락 시, null 값을 제거 후 반환한다.")
    @Test
    void recommend_filter_null_success() {
        //given
        Long userId = 1L;
        List<Long> ids = List.of(1L, 2L, 3L);
        when(recommendationRepository.find(userId))
                .thenReturn(ids);

        //2번 유저 null
        List<UserRecommendResponse> queryResult = List.of(
                createDto(1L, "user1"),
                createDto(3L, "user3")
        );

        when(userProfileQueryRepository.findRecommendUsers(ids))
                .thenReturn(queryResult);

        //when
        List<UserRecommendResponse> result =
                userRecommendService.recommend(userId);

        //then
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(UserRecommendResponse::getId)
                .containsExactly(1L, 3L);
    }

    // ======

    private UserRecommendResponse createDto(Long id, String nickname) {
        return UserRecommendResponse.builder()
                .id(id)
                .nickname(nickname)
                .imageUrl("img")
                .age(20)
                .location(
                        UserLocationResponse.builder()
                                .siDoId(1L)
                                .siDoName("서울")
                                .siGunGuId(101L)
                                .siGunGuName("강남구")
                                .build()
                )
                .usingLanguages(new ArrayList<>())
                .learningLanguages(new ArrayList<>())
                .build();
    }
}