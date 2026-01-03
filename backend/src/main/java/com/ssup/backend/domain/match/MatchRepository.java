package com.ssup.backend.domain.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    boolean existsByRequesterIdAndReceiverIdAndStatusIn(Long requesterId, Long receiverId, List<MatchStatus> pending);

    //특정 유저간의 매치 기록 가져오기
    @Query("""
    select m from Match m
    where ((m.requester.id = :user1Id and m.receiver.id = :user2Id)
       or (m.requester.id = :user2Id and m.receiver.id = :user1Id))
    and m.status IN ('PENDING', 'ACCEPTED', 'REJECTED')
    """)
    Optional<Match> findActiveMatchBetweenUsers(
            @Param("user1Id") Long user1Id,
            @Param("user2Id") Long user2Id
    );

    //특정 유저끼리 이미 진행 중인 매치가 있는지
    @Query("""
            select count(m) > 0 from Match m
            where ((m.requester.id = :requesterId AND m.receiver.id = :receiverId)
            or (m.requester.id = :receiverId AND m.receiver.id = :requesterId))
            and m.status IN :statuses
            """)
    boolean existsActiveMatch(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId, @Param("statuses") List<MatchStatus> statuses);

    //나의 모든 매칭 내역 조회 (최신순)
    @Query("""
            select m from Match m
            where m.requester.id = :userId or m.receiver.id = :userId
            order by m.createdAt desc
            """)
    List<Match> findAllByUserId(@Param("userId") Long userId);
}
