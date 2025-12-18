package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("local")
public class PostDummyInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        User user = userRepository.save(
                User.builder()
                        .nickname("유저1")
                        .email("user1@gmail.com")
                        .intro("hi hello")
                        .build()
        );

        if (postRepository.count() > 0) return;

        postRepository.save(
                Post.builder()
                        .author(user)
                        .title("첫 번째 게시글")
                        .content("프론트 상세 페이지 테스트용")
                        .usingLanguage("Japanese")
                        .learningLanguage("Korean")
                        .build()
        );

        postRepository.save(
                Post.builder()
                        .author(user)
                        .title("두 번째 게시글")
                        .content("두 번째 글 내용")
                        .usingLanguage("Korean")
                        .learningLanguage("English")
                        .build()
        );
    }
}
