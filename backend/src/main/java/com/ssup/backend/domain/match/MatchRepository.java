package com.ssup.backend.domain.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    boolean existsByRequesterIdAndReceiverIdAndStatusIn(Long requesterId, Long receiverId, List<MatchStatus> pending);

    //특정 유저끼리 이미 진행 중인 매치가 있는지
    @Query("""
            select count(m) > 0 from Match m
            where m.requester.id = :rId AND m.receiver.id = :vId
            or m.requester.id = :vId AND m.receiver.id = :rId
            and m.status IN :statuses
            """)
    boolean existsActiveMatch(@Param("rId") Long requesterId, @Param("vId") Long receiverId, @Param("statuses") List<MatchStatus> statuses);

    //나의 모든 매칭 내역 조회 (최신순)
    @Query("""
            select m from Match m
            where m.requester.id = :userId or m.receiver.id = :userId
            order by m.createdAt desc
            """)
    List<Match> findAllByUserId(@Param("userId") Long userId);
}
