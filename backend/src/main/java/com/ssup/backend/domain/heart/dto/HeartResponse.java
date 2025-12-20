package com.ssup.backend.domain.heart.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HeartResponse {

    private final boolean hearted;   //현재 내가 누른 상태인지
    private final long heartCount;   //최신 좋아요 수

    public static HeartResponse of(boolean hearted, long heartCount) {
        return new HeartResponse(hearted, heartCount);
    }
}
