package com.ssup.backend.domain.user.slice;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.language.UserLanguage;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager tem;
    private EntityManager em;

    @BeforeEach
    void setUp() {
        this.em = tem.getEntityManager();
    }

    @DisplayName("이메일/닉네임으로 유저 조회 - 성공")
    @Test
    void existsByEmailAndNickname_success() {
        //given
        User user = saveAndGetUser();

        //when, then
        assertThat(userRepository.existsByEmail(user.getEmail())).isTrue();
        assertThat(userRepository.existsByNickname(user.getNickname())).isTrue();
    }

    @DisplayName("나의 프로필 조회 시, 연관된 엔티티를 fetch join 한다.")
    @Test
    void findMeProfileById_fetchJoin_success() {
        //given
        User user = saveAndGetUser();
        InterestCategory category = tem.persist(saveAndGetInterestCategory());
        Interest interest = saveAndGetInterest("헬스", category);
        user.addInterest(interest);
        em.persist(user);

        //when
        User findUser = userRepository.findMeProfileById(user.getId()).get();

        //then
        assertThat(findUser.getInterests()).hasSize(1);
        assertThat(findUser.getInterests().get(0).getInterest().getCategory().getName())
                .isEqualTo("운동");
    }

    @DisplayName("타인의 프로필 조회 시, 연관된 엔티티를 fetch join 한다.")
    @Test
    void findUserProfileById_fetchJoin_success() {
        //given
        InterestCategory category = InterestCategory.builder().code("SPORTS").name("농구").build();
        tem.persist(category);
        Interest interest = saveAndGetInterest("영화", category);
        User user = saveAndGetUser();
        user.addInterest(interest);
        em.persist(user);

        //when
        User findUser = userRepository.findUserProfileById(user.getId()).get();

        //then
        assertThat(findUser.getInterests()).hasSize(1);
    }

    @DisplayName("프로필 조회 시, 연관된 언어 엔티티를 fetch join 한다.")
    @Test
    void findWithLanguages_fetchJoin_success() {
        //given
        Language language = tem.persist(Language.builder().code("KO").name("Korean").build());
        User user = saveAndGetUser();
        em.persist(new UserLanguage(user, language, LanguageLevel.BEGINNER, LanguageType.LEARNING));

        //when
        User findUser = userRepository.findWithLanguages(user.getId()).get();

        //then
        assertThat(findUser.getLanguages()).hasSize(1);
        assertThat(findUser.getLanguages().get(0).getLanguage().getName())
                .isEqualTo("Korean");
    }

    //===== init ======

    private User saveAndGetUser() {
        User user = User.builder()
                .nickname("james")
                .email("email123@gmail.com")
                .build();

        em.persist(user);
        em.flush();
        em.clear();

        return em.find(User.class, user.getId());
    }

    private InterestCategory saveAndGetInterestCategory() {
        return tem.persist(InterestCategory.builder()
                .code("SPORTS")
                .name("운동")
                .build());
    }

    private Interest saveAndGetInterest(String name, InterestCategory category) {
        return tem.persist(Interest.builder()
                .code(name.toUpperCase())
                .name(name)
                .category(category)
                .build());
    }
}
