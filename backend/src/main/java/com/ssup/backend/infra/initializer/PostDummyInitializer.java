package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

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

        Random random = new Random();
        int min = 1;
        int max = 300;

        for (int i = 1; i < 31; i++) {
            int viewCount = random.nextInt(max - min + 1) + min;

            postRepository.save(
                    Post.builder()
                            .author(user)
                            .title(i + "번째 게시글")
                            .content(i + "번째 프론트 상세 페이지 테스트용")
                            .usingLanguage("Japanese")
                            .learningLanguage("Korean")
                            .viewCount(viewCount)
                            .build()
            );
        }
    }
}
