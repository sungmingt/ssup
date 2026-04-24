package com.ssup.backend.domain.user.slice;
//
//import com.ssup.backend.domain.user.Gender;
//import com.ssup.backend.fixture.user.UserJpaFixture;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.DisplayName;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.ssup.backend.domain.user.recommend.OpenAiClient;
//import com.ssup.backend.domain.user.recommend.UserProfileEmbedding;
//import com.ssup.backend.domain.user.recommend.UserProfileEmbeddingRepository;
//import com.ssup.backend.domain.user.User;
//import com.ssup.backend.domain.user.UserRepository;
//import com.ssup.backend.domain.user.profile.UserProfileService;
//import com.ssup.backend.domain.user.profile.dto.UserProfileUpdateRequest;
//import org.awaitility.Awaitility;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Duration;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//@Commit
//class UserProfileUpdateEmbeddingTest {
//
//    @Autowired
//    private UserProfileService userService;
//
//    @Autowired
//    private UserProfileEmbeddingRepository embeddingRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @MockBean
//    private OpenAiClient openAiClient;
//
//    @Autowired
//    private EntityManager em;
//
//    @DisplayName("프로필 수정 시 embedding 생성 후 redis에 저장된다.")
//    @Test
//    void createAndSaveEmbedding_whenProfileChange_success() {
//        //given
//        User user = UserJpaFixture.createUser(em);
//        given(openAiClient.embed(anyString()))
//                .willReturn(List.of(0.1, 0.2, 0.3));
//
//        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
//                "nick", null, "intro", 20, Gender.MALE,
//                "010", false, null, null
//        );
//
//        //when
//        userService.updateMyProfile(
//                user.getId(),
//                null,
//                request
//        );
//
//        //then(async 대기)
//        Awaitility.await()
//                .atMost(Duration.ofSeconds(5))
//                .until(() ->
//                        embeddingRepository.findByUserId(user.getId()).isPresent()
//                );
//
//        UserProfileEmbedding embedding =
//                embeddingRepository.findByUserId(user.getId()).orElseThrow();
//
//        assertThat(embedding.getVector())
//                .containsExactly(0.1, 0.2, 0.3);
//    }
//}