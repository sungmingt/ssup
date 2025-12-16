package com.ssup.backend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateRequest {

    private String title;
    private String content;
    private List<String> keepImageUrls; //기존의 이미지 중 유지될 이미지
}