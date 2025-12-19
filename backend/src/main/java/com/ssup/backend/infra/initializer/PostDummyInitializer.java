package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageRepository;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
//@Profile("local")
@Order(2)
public class PostDummyInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

    @Override
    public void run(String... args) {
        User user = userRepository.save(
                User.builder()
                        .nickname("유저1")
                        .email("user1@gmail.com")
                        .intro("hi hello")
                        .build()
        );

        Random randomViewCount = new Random();
        int minViewCount = 1;
        int maxViewCount = 300;

        Language usingLanguage = languageRepository.findById(2L).get();
        Language learningLanguage = languageRepository.findById(4L).get();

        for (int i = 1; i < 31; i++) {
            int viewCount = randomViewCount.nextInt(maxViewCount - minViewCount + 1) + minViewCount;

            postRepository.save(
                    Post.builder()
                            .author(user)
                            .title(i + "번째 게시글")
                            .content(i + "번째 프론트 상세 페이지 테스트용")
                            .usingLanguage(usingLanguage.getName())
                            .learningLanguage(learningLanguage.getName())
                            .viewCount(viewCount)
                            .build()
            );
        }
    }
}
