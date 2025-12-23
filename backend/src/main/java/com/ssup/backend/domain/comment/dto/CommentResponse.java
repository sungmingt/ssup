package com.ssup.backend.domain.comment.dto;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;

    private Long postId;

    private Long authorId;

    private String authorImageUrl;

    private String authorName;

    private String content;

    private String imageUrl;

    private long heartCount;

//    private boolean hearted;

    private LocalDateTime createdAt;

    public static CommentResponse of(Comment comment) {
        User author = comment.getAuthor();

        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .authorId(author.getId())
                .authorImageUrl(author.getImageUrl())
                .authorName(author.getNickname())
                .content(comment.getContent())
                .imageUrl(comment.getImageUrl())
                .heartCount(comment.getHeartCount())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

