package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.post.Post;
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
public class PostListResponse {

    private Long id;

    private String usingLanguage;

    private String learningLanguage;

    private String authorName;

    private String authorImageUrl;

    private String title;

    private String content;

    private long commentCount;

    private long heartCount;

    private long viewCount;

    private boolean heartedByMe;

    private String thumbnailImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static List<PostListResponse> of(List<Post> postList, Set<Long> heartedPostIds) {

        return postList.stream()
                .map(post -> PostListResponse.builder()
                                .id(post.getId())
                                .thumbnailImageUrl(post.getImageUrls().isEmpty() ? null : post.getImageUrls().get(0))
                                .authorName(post.getAuthor().getNickname())
                                .authorImageUrl(post.getAuthor().getImageUrl())
                                .usingLanguage(post.getUsingLanguage())
                                .learningLanguage(post.getLearningLanguage())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .viewCount(post.getViewCount())
                                .heartedByMe(heartedPostIds.contains(post.getId()))
                                .heartCount(post.getHeartCount())
                                .commentCount(post.getCommentCount())
                                .createdAt(post.getCreatedAt())
                                .build()
                )
                .toList();

    }
}
