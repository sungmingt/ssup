package com.ssup.backend.domain.user.slice;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.fixture.user.UserJpaFixture;
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

    @DisplayName("이메일/닉네임으로 유저 조회 - 성공")
    @Test
    void existsByEmailAndNickname_success() {
        //given
        User user = UserJpaFixture.createUser(tem.getEntityManager());

        //when, then
        assertThat(userRepository.existsByEmail(user.getEmail())).isTrue();
        assertThat(userRepository.existsByNickname(user.getNickname())).isTrue();
    }

    @DisplayName("나의 프로필 조회 시, 연관된 엔티티를 fetch join 한다.")
    @Test
    void findMeProfileById_fetchJoin_success() {
        //given
        User user = UserJpaFixture.createUser(tem.getEntityManager());
        InterestCategory category = saveAndGetInterestCategory();
        Interest interest = saveAndGetInterest("헬스", category);
        user.addInterest(interest);
        tem.flush();
        tem.clear();

        //when
        User findUser = userRepository.findMeProfileById(user.getId()).get();

        //then
        assertThat(findUser.getInterests()).hasSize(1);
        assertThat(findUser.getInterests())
                .extracting(ui -> ui.getInterest().getCategory().getName())
                .contains("운동");
    }

    @DisplayName("타인의 프로필 조회 시, 연관된 엔티티를 fetch join 한다.")
    @Test
    void findUserProfileById_fetchJoin_success() {
        //given
        InterestCategory category = InterestCategory.builder().code("SPORTS").name("농구").build();
        tem.persist(category);
        Interest interest = saveAndGetInterest("영화", category);
        User user = UserJpaFixture.createUser(tem.getEntityManager());
        user.addInterest(interest);
        tem.persist(user);

        //when
        User findUser = userRepository.findUserProfileById(user.getId()).get();

        //then
        assertThat(findUser.getInterests()).hasSize(1);
    }

    @DisplayName("프로필 조회 시, 연관된 언어 엔티티를 fetch join 한다.")
    @Test
    void findWithLanguages_fetchJoin_success() {
        //given
//        Language language = tem.persist(Language.builder().code("KO").name("Korean").build());
        User user = UserJpaFixture.createUser(tem.getEntityManager());
//        tem.persist(new UserLanguage(user, language, LanguageLevel.BEGINNER, LanguageType.LEARNING));

        //when
        User findUser = userRepository.findWithLanguages(user.getId()).get();

        //then
        assertThat(findUser.getLanguages()).hasSize(2);
        assertThat(findUser.getLanguages())
                .extracting(ul -> ul.getLanguage().getName())
                .contains("한국어");
    }

    @DisplayName("삭제된 계정은 더이상 조회 쿼리에 포함되지 않는다.")
    @Test
    void deletedAccount_notContainedInFindQueryAnymore() {
        //given
        User user = UserJpaFixture.createUser(tem.getEntityManager());

        //when
        user.delete();

        //then
        assertThat(userRepository.findUserProfileById(user.getId())).isEmpty();
    }

    //===== init ======

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
