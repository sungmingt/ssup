package com.ssup.backend.domain.heart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HeartResponse {

    private boolean hearted;   //현재 내가 누른 상태인지
    private long heartCount;   //최신 좋아요 수

    public static HeartResponse of(boolean hearted, long heartCount) {
        return new HeartResponse(hearted, heartCount);
    }
}
