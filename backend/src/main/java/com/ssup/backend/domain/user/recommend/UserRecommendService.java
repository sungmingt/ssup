package com.ssup.backend.domain.user.recommend;

import com.ssup.backend.domain.user.recommend.dto.UserRecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRecommendService {

    //유저의 추천 친구 리스트(userId List)를 조회 후, dto로 변환하여 반환한다.

    private final UserRecommendRepository recommendationRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;

    public List<UserRecommendResponse> recommend(Long userId) {
        List<Long> ids = recommendationRepository.find(userId);

        if (ids.isEmpty()) return List.of();

        List<UserRecommendResponse> result = userProfileQueryRepository.findRecommendUsers(ids);

        //dto 매핑
        Map<Long, UserRecommendResponse> userMap = result.stream()
                .collect(Collectors.toMap(UserRecommendResponse::getId, r -> r));

        //추천 순서 유지
        return ids.stream()
                .map(userMap::get)
                .filter(Objects::nonNull)
                .toList();
    }
}