package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("local")
public class PostDummyInitializer implements CommandLineRunner {

    private final PostRepository postRepository;

    @Override
    public void run(String... args) {
        if (postRepository.count() > 0) return;

        postRepository.save(
                Post.builder()
                        .title("첫 번째 게시글")
                        .content("프론트 상세 페이지 테스트용")
                        .usingLanguage("Japanese")
                        .learningLanguage("Korean")
                        .build()
        );

        postRepository.save(
                Post.builder()
                        .title("두 번째 게시글")
                        .content("두 번째 글 내용")
                        .usingLanguage("Korean")
                        .learningLanguage("English")
                        .build()
        );
    }
}
