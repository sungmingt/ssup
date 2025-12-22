package com.ssup.backend.domain.heart.comment;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "comment_heart",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_comment_heart_user_comment",
                columnNames = {"comment_id", "user_id"}
        )
)
public class CommentHeart {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Version
    private Long version;

    public CommentHeart(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}