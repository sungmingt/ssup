package com.ssup.backend.domain.heart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "좋아요한 글 목록 DTO")
public class HeartedPostResponse {

    private Long postId;

    private String authorId;

    private String title;

    private String content;

    private String authorNickname;

    private List<String> imageUrls;

    private int viewCount;

    private LocalDateTime createdAt;
}

