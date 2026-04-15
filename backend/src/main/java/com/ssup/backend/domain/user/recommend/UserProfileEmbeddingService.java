package com.ssup.backend.domain.user.recommend;

import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.recommend.util.UserProfileTextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileEmbeddingService {

    private final OpenAiClient openAiClient;
    private final UserRepository userRepository;
    private final UserProfileEmbeddingRepository repository;
    private final UserProfileTextBuilder profileTextBuilder;

    //embedding API 호출/생성/저장
    public void rebuild(Long userId) {
        User user = userRepository.findUserProfileById(userId)
                .orElseThrow();

        String profileText = profileTextBuilder.build(user);
        log.info("### profileText:{}", profileText);

        log.info("### OpenAI embedding 요청 호출");
        List<Double> embedding = openAiClient.embed(profileText);

//        List<Double> embedding = List.of(
//                (double) profileText.length() % 10,
//                (double) profileText.hashCode() % 100 / 100.0,
//                Math.random()
//        );

        log.info("### created embedding:{}", embedding);
        repository.save(
                new UserProfileEmbedding(
                        userId,
                        embedding,
                        LocalDateTime.now()
                )
        );

        UserProfileEmbedding userProfileEmbedding = repository.findByUserId(userId).get();
        log.info("### saved userProfileEmbedding:{}", userProfileEmbedding);
    }
}