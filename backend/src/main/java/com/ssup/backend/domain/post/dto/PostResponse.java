package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;

    private List<String> imageUrls;

//    private List<CommentResponse> comments;

//    private List<HeartResponse> comments;

    private String usingLanguage;

    private String learningLanguage;

    private String authorName;

    private String authorImageUrl;

    private String title;

    private String content;

    private int commentCount;

    private long heartCount;

    private long viewCount;

    private boolean heartedByMe;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PostResponse of(User author, Post post, boolean heartedByMe) {
        return new PostResponse(post.getId(), new ArrayList<>(post.getImageUrls()), post.getUsingLanguage(), post.getLearningLanguage(), author.getNickname(),
                author.getImageUrl(), post.getTitle(), post.getContent(),
                0, post.getHeartCount(), post.getViewCount(), heartedByMe, post.getCreatedAt(), post.getUpdatedAt());
    }
}
