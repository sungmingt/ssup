package com.ssup.backend.domain.post;

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
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

//    @OneToMany(mappedBy = "post")
//    private List<Comment> comments = new ArrayList<>();

//    @OneToMany(mappedBy = "post")
//    private List<Heart> hearts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "post_images",
            joinColumns = @JoinColumn(name = "post_id")
    )
    @Column(name = "image_url")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String usingLanguage;

    private String learningLanguage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private long viewCount;

    public void setAuthor(User author) {
        this.author = author;
        author.getPosts().add(this);
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void replaceImages(List<String> keepImageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(keepImageUrls);
    }

    public void addImages(List<String> newImageUrls) {
        this.imageUrls.addAll(newImageUrls);
    }
}
