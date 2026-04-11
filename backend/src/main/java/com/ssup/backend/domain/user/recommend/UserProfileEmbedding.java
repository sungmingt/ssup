package com.ssup.backend.domain.user.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEmbedding {

    private Long userId;
    private List<Double> vector;
    private LocalDateTime updatedAt;
}
