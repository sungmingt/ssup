package com.ssup.backend.domain.match;

import com.ssup.backend.domain.user.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EntityManager em;

    private User userA;
    private User userB;

    @BeforeEach
    void setUp() {
        userA = User.builder().nickname("UserA").contact("010-1").build();
        userB = User.builder().nickname("UserB").contact("010-2").build();
        em.persist(userA);
        em.persist(userB);
    }

    @Test
    @DisplayName("existsActiveMatch - 맞신청 상황에서도 중복을 찾아내야 한다")
    void existsActiveMatch_CrossRequest() {
        //given (A가 B에게 신청)
        Match match = Match.builder()
                .requester(userA)
                .receiver(userB)
                .status(MatchStatus.PENDING)
                .build();
        matchRepository.save(match);

        //when (B가 A에게 신청)
        boolean exists = matchRepository.existsActiveMatch(
                userB.getId(),
                userA.getId(),
                List.of(MatchStatus.PENDING, MatchStatus.ACCEPTED)
        );

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("findAllByUserId - 내가 신청한 매치와 다른 사람에게 받은 매치를 모두 조회한다")
    void findAllByUserId_Success() {
        //given
        User userC = User.builder().nickname("UserC").contact("010-3").build();
        em.persist(userC);

        Match sent = Match.builder().requester(userA).receiver(userB).status(MatchStatus.PENDING).build();
        Match received = Match.builder().requester(userC).receiver(userA).status(MatchStatus.PENDING).build();

        matchRepository.save(sent);
        matchRepository.save(received);

        //when
        List<Match> myMatchHistory = matchRepository.findAllByUserId(userA.getId());

        //then
        assertThat(myMatchHistory).hasSize(2);
        assertThat(myMatchHistory.get(0).getRequester()).isEqualTo(userA);
        assertThat(myMatchHistory.get(1).getReceiver()).isEqualTo(userA);
    }
}