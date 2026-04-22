package com.ssup.backend.domain.user.recommend.dto;

import com.ssup.backend.domain.user.profile.dto.UserLocationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecommendResponse {

    private Long id;
    private String nickname;
    private String imageUrl;
    private int age;
    private UserLocationResponse location;
    private List<String> usingLanguages;
    private List<String> learningLanguages;

    public static UserRecommendResponse of(QUserRecommendResponse r) {
        return UserRecommendResponse.builder()
                .id(r.getId())
                .nickname(r.getNickname())
                .imageUrl(r.getImageUrl())
                .age(r.getAge())
                .location(UserLocationResponse.builder()
                        .siDoId(r.getSiDoId())
                        .siDoName(r.getSiDoName())
                        .siGunGuId(r.getSiGunGuId())
                        .siGunGuName(r.getSiGunGuName())
                        .build())
                .usingLanguages(r.getUsingLanguages())
                .learningLanguages(r.getLearningLanguages())
                .build();
    }
}
