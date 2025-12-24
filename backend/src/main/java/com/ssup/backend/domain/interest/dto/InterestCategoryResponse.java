package com.ssup.backend.domain.interest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterestCategoryResponse {

    private Long id;
    private String code;
    private String name;
}
