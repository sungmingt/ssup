package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.post.Post;
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
public class PostListResponse {

    private Long id;

    private String usingLanguage;

    private String learningLanguage;

    private String authorName;

    private String authorImageUrl;

    private String title;

    private String content;

    private int commentCount;

    private int heartCount;

    private long viewCount;

    private String thumbnailImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static List<PostListResponse> of(List<Post> postList) {

        return postList.stream()
                .map(post -> PostListResponse.builder()
                                .id(post.getId())
                                .authorName(post.getAuthor().getNickname())
                                .authorImageUrl(post.getAuthor().getImageUrl())
//                                .usingLanguage(post.getUsingLanguage())
//                                .learningLanguage(post.getLearningLanguage())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .viewCount(post.getViewCount())
//                                .heartCount(post.getHearts.size())
//                                .commentCount(post.getComments.size())
                                .createdAt(post.getCreatedAt())
                                .build()
                )
                .toList();

    }
}
