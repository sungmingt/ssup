package com.ssup.backend.domain.heart.post;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    @Query("""
    select ph.post.id
    from PostHeart ph
    where ph.user.id = :userId
      and ph.post.id in :postIds
    """)
    List<Long> findHeartedPostIds(
            @Param("userId") Long userId,
            @Param("postIds") List<Long> postIds
    );
//
//    @Modifying
//    @Query("""
//    update Post p
//    set p.heartCount = p.heartCount + 1
//    where p.id = :postId
//    """)
//    void increaseHeartCount(@Param("postId") Long postId);

    void deleteByPostAndUser(Post post, User user);
}