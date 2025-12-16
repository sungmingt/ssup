package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {

    private String usingLanguage;

    private String learningLanguage;

    private String title;

    private String content;

    public Post toEntity() {
        return Post.builder()
                .usingLanguage(this.usingLanguage)
                .learningLanguage(this.learningLanguage)
                .title(this.title)
                .content(this.content)
                .viewCount(1)
                .build();
    }
}
