package com.ssup.backend.domain.heart.post;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "post_heart",
        uniqueConstraints = {
                @UniqueConstraint( //같은 유저의 중복 좋아요 방지
                        name = "uk_post_heart_user_post",
                        columnNames = {"user_id", "post_id"}
                )
        }
)
public class PostHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Version
    private Long version;

    public PostHeart(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
