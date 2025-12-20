package com.ssup.backend.domain.heart.post;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    boolean existsByPostAndUser(Post post, User user);
    void deleteByPostAndUser(Post post, User user);
}