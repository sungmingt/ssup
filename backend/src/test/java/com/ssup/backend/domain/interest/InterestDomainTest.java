package com.ssup.backend.domain.interest;

import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.fixture.user.UserJpaFixture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
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
class InterestDomainTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager tem;

    private EntityManager em;

    @BeforeEach
    void setUp() {
        this.em = tem.getEntityManager();
    }

    @DisplayName("Interest는 Category 없이 저장할 수 없다")
    @Test
    void interest_without_category_fail() {
        Interest interest = Interest.builder()
                .code("SPORT")
                .name("운동")
                .category(null)
                .build();

        assertThatThrownBy(() -> {
            em.persist(interest);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @DisplayName("User에서 Interest 제거 시, 모든 UserInterest가 DB에서 삭제되어야 한다.")
    @Test
    void orphanRemoval_userInterest_success() {
        //given
        InterestCategory category = tem.persist(
                InterestCategory.builder().code("HOBBY").name("취미").build()
        );
        Interest interest = tem.persist(
                Interest.builder().code("SPORT").name("운동").category(category).build()
        );

        User user = UserJpaFixture.createUser(em);
        user.addInterest(interest);
        em.flush();
        em.clear();

        //when
        User findUser = em.find(User.class, user.getId());
        findUser.getInterests().clear();
        em.flush();
        em.clear();

        //then
        Long count = em.createQuery(
                "select count(ui) from UserInterest ui", Long.class
        ).getSingleResult();

        assertThat(count).isZero();
    }

}