package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    private String imageUrl;

    private int viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static List<PostListResponse> of(List<Post> postList) {

        return postList.stream()
                .map(post -> new PostListResponse(
                        post.getId(), post.getUsingLanguage(), post.getLearningLanguage(),
                        post.getAuthor().getNickname(), post.getAuthor().getImageUrl(),
                        post.getTitle(), post.getContent(), 0, 0, "null", 0,
                        post.getCreatedAt(), post.getUpdatedAt()
                        )
                )
                .toList();
    }
}
