package com.ssup.backend.domain.comment.dto;

import com.ssup.backend.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;

    private Long authorId;

    private String content;

    private String imageUrl;

    private long heartCount;

    private LocalDateTime createdAt;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .content(comment.getContent())
                .imageUrl(comment.getImageUrl())
                .heartCount(comment.getHeartCount())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

