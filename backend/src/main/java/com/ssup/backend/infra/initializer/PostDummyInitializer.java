package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageRepository;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.Gender;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
//@Profile("!test")
@Profile("local")
@Order(4)
public class PostDummyInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final LocationRepository locationRepository;
    private final InterestRepository interestRepository;

    @Override
    public void run(String... args) {
        Interest interest = interestRepository.findById(1L).get();
        Location location = locationRepository.findByLevel(2).get(0);
        Language usingLanguage = languageRepository.findById(1L).get();
        Language learningLanguage = languageRepository.findById(2L).get();

        User user = User.builder()
                .nickname("유저1")
                .email("user1@gmail.com")
                .intro("hi hello")
                .contact("instagram@hihi")
                .age(30)
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE)
                .location(location)
                .build();

        user.addLanguage(usingLanguage, LanguageLevel.NATIVE, LanguageType.USING);
        user.addLanguage(learningLanguage, LanguageLevel.ADVANCED, LanguageType.LEARNING);
        user.addLanguage(languageRepository.findById(3L).get(), LanguageLevel.ADVANCED, LanguageType.USING);
        user.addLanguage(languageRepository.findById(4L).get(), LanguageLevel.ADVANCED, LanguageType.LEARNING);
        user.addLanguage(languageRepository.findById(5L).get(), LanguageLevel.ADVANCED, LanguageType.LEARNING);
        user.addInterest(interest);

        User savedUser = userRepository.save(user);

        Random randomViewCount = new Random();
        int minViewCount = 1;
        int maxViewCount = 300;

        for (int i = 1; i < 31; i++) {
            int viewCount = randomViewCount.nextInt(maxViewCount - minViewCount + 1) + minViewCount;

            postRepository.save(
                    Post.builder()
                            .author(savedUser)
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
