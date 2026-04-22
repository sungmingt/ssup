package com.ssup.backend.domain.user.slice;

import com.ssup.backend.QuerydslTestConfig;
import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.language.UserLanguage;
import com.ssup.backend.domain.user.recommend.UserProfileQueryRepository;
import com.ssup.backend.domain.user.recommend.dto.UserRecommendResponse;
import com.ssup.backend.fixture.language.LanguageJpaFixture;
import com.ssup.backend.fixture.location.LocationJpaFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({UserProfileQueryRepository.class, QuerydslTestConfig.class})
class UserProfileQueryRepositoryTest {

    @Autowired
    private TestEntityManager tem;

    @Autowired
    private UserProfileQueryRepository queryRepository;

    private EntityManager em;

    @BeforeEach
    void setUp() {
        this.em = tem.getEntityManager();
    }

    @DisplayName("추천 유저 조회 - DTO projection 성공")
    @Test
    void findRecommendUsers_success() {
        //given
        Language ko = LanguageJpaFixture.createKorean(em);
        Language en = LanguageJpaFixture.createEnglish(em);
        Location siDo = LocationJpaFixture.createSiDo(em);
        Location siGunGu = LocationJpaFixture.createSiGunGu(em, siDo);

        User user1 = createUser("user1", siGunGu);
        User user2 = createUser("user2", siGunGu);

        //user1: KO + EN
        addLanguage(user1, ko, LanguageType.USING);
        addLanguage(user1, en, LanguageType.LEARNING);

        //user2: EN + KO
        addLanguage(user2, en, LanguageType.USING);
        addLanguage(user2, ko, LanguageType.LEARNING);

        em.flush();
        em.clear();

        List<Long> ids = List.of(user1.getId(), user2.getId());

        //when
        List<UserRecommendResponse> result = queryRepository.findRecommendUsers(ids);

        //then
        assertThat(result).hasSize(2);

        UserRecommendResponse dto1 = result.stream()
                .filter(u -> u.getId().equals(user1.getId()))
                .findFirst()
                .orElseThrow();

        UserRecommendResponse dto2 = result.stream()
                .filter(u -> u.getId().equals(user2.getId()))
                .findFirst()
                .orElseThrow();

        // ===== user1 검증 =====
        assertThat(dto1.getNickname()).isEqualTo(user1.getNickname());
        assertThat(dto1.getUsingLanguages()).containsExactly("한국어");
        assertThat(dto1.getLearningLanguages()).containsExactly("English");
        assertThat(dto1.getLocation().getSiGunGuName()).isEqualTo("강남구");

        // ===== user2 검증 =====
        assertThat(dto2.getUsingLanguages()).containsExactly("English");
    }

    @DisplayName("언어가 여러개일때 중복 없이 조회된다.")
    @Test
    void findRecommendUsers_language_aggregation_success() {
        //given
        Language ko = LanguageJpaFixture.createKorean(em);
        Language en = LanguageJpaFixture.createEnglish(em);
        Location siDo = LocationJpaFixture.createSiDo(em);
        Location siGunGu = LocationJpaFixture.createSiGunGu(em, siDo);

        User user = createUser("user", siGunGu);

        addLanguage(user, ko, LanguageType.USING);
        addLanguage(user, en, LanguageType.LEARNING);

        em.flush();
        em.clear();

        //when
        List<UserRecommendResponse> result =
                queryRepository.findRecommendUsers(List.of(user.getId()));

        //then
        UserRecommendResponse dto = result.get(0);

        assertThat(dto.getUsingLanguages()).contains("한국어");

        assertThat(dto.getLearningLanguages()).containsExactly("English");
    }

    // ======

    private User createUser(String nickname, Location location) {
        User user = User.builder()
                .email(nickname + "@test.com")
                .nickname(nickname)
                .location(location)
                .build();

        em.persist(user);
        return user;
    }

    private void addLanguage(User user, Language language, LanguageType type) {
        UserLanguage ul = UserLanguage.builder()
                .user(user)
                .language(language)
                .type(type)
                .level(LanguageLevel.BEGINNER)
                .build();

        em.persist(ul);
    }
}