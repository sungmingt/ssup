package com.ssup.backend.domain.post;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.heart.post.PostHeart;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<PostHeart> hearts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "post_images",
            joinColumns = @JoinColumn(name = "post_id")
    )
    @Column(name = "image_url")
    @Builder.Default //builder 생성 시 null 방지
    private List<String> imageUrls = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String usingLanguage;

    private String learningLanguage;

    private long viewCount;

    private long commentCount;

    private long heartCount;

    public void setAuthor(User author) {
        this.author = author;
        author.getPosts().add(this);
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content, String usingLanguage, String learningLanguage) {
        this.title = title;
        this.content = content;
        this.usingLanguage = usingLanguage;
        this.learningLanguage = learningLanguage;
    }

    public void replaceImages(List<String> keepImageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(keepImageUrls);
    }

    public void addImages(List<String> newImageUrls) {
        this.imageUrls.addAll(newImageUrls);
    }

    public void increaseHeartCount() {
        this.heartCount++;
    }

    public void decreaseHeartCount() {
        if (this.heartCount > 0) this.heartCount--;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) this.commentCount--;
    }
}
