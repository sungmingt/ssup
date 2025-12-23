package com.ssup.backend.domain.comment.dto;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListResponse {

    private Long id;

    private Long postId;

    private Long authorId;

    private String authorImageUrl;

    private String authorName;

    private String content;

    private String imageUrl;

    private long heartCount;

    private boolean hearted;

    private LocalDateTime createdAt;

    public static List<CommentListResponse> of(List<Comment> comments, Set<Long> heartedCommentIds) {

        return comments.stream()
                .map(comment -> CommentListResponse.builder()
                        .id(comment.getId())
                        .postId(comment.getPost().getId())
                        .authorId(comment.getAuthor().getId())
                        .authorImageUrl(comment.getAuthor().getImageUrl())
                        .authorName(comment.getAuthor().getNickname())
                        .content(comment.getContent())
                        .imageUrl(comment.getImageUrl())
                        .heartCount(comment.getHeartCount())
                        .hearted(heartedCommentIds.contains(comment.getId()))
                        .createdAt(comment.getCreatedAt())
                        .build()
                )
                .toList();
    }
}

