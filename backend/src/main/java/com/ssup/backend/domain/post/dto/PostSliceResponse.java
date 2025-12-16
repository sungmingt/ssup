package com.ssup.backend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSliceResponse {

    private List<PostListResponse> items;

    private Long nextCursorKey;

    private Long nextCursorId;

    private boolean hasNext;
}
