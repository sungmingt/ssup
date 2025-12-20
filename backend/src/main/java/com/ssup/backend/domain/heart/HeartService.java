package com.ssup.backend.domain.heart;

import com.ssup.backend.domain.heart.dto.HeartResponse;

public interface HeartService {
    HeartResponse toggleHeart(Long targetId, Long userId);
}