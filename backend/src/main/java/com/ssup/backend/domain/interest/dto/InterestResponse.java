package com.ssup.backend.domain.interest.dto;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.interest.UserInterest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterestResponse {

    private Long id;
    private String code;
    private String name;
    private InterestCategoryResponse category;

    public static InterestResponse of(Interest interest) {
        InterestCategory category = interest.getCategory();

        InterestCategoryResponse categoryResponse = InterestCategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .build();

        return InterestResponse.builder()
                .id(interest.getId())
                .code(interest.getCode())
                .name(interest.getName())
                .category(categoryResponse)
                .build();
    }
}
