package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.post.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateRequest {

    @NotBlank(message = "사용 언어를 입력해주세요.")
    private String usingLanguage;

    @NotBlank(message = "학습 언어를 입력해주세요.")
    private String learningLanguage;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
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
