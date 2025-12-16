package com.ssup.backend.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //Java 15+ TextBlock 문법 사용

    //최신순
    @Query("""
        select p
        from Post p
        where (:cursorId is null or p.id < :cursorId)
        order by p.id desc
    """)
    List<Post> findLatest(@Param("cursorId") Long cursorId, Pageable pageable
    );

    //조회수순
    @Query("""
        select p
        from Post p
        where (
            (:cursorKey is null)
            or (p.viewCount < :cursorKey)
            or (p.viewCount = :cursorKey and p.id < :cursorId)
        )
        order by p.viewCount desc, p.id desc
    """)
    List<Post> findByViews(
            @Param("cursorKey") Long cursorKey,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
