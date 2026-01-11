package com.ssup.backend.domain.post;

public record PostFilterCondition(
        Long locationId,
        String usingLanguage,
        String learningLanguage,
        Long interestId
) {
    //아무 조건도 선택하지 않았을 때(전체 조회)
    public static PostFilterCondition empty() {
        return new PostFilterCondition(null, null, null, null);
    }
}