package com.ssup.backend.domain.user.recommend.dto;

import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecommendResponse {

    private Long id;
    private String nickname;
    private String imageUrl;
    private String intro;

    public static UserRecommendResponse of(User user) {
        return UserRecommendResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .intro(user.getIntro())
                .build();
    }
}
