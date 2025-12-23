package com.ssup.backend.domain.heart.comment;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {

    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    @Query("""
    select ch.comment.id
    from CommentHeart ch
    where ch.user.id = :userId
      and ch.comment.id in :commentIds
    """)
    List<Long> findHeartedCommentIds(
            @Param("userId") Long userId,
            @Param("commentIds") List<Long> commentIds
    );

    void deleteByCommentAndUser(Comment comment, User user);
}