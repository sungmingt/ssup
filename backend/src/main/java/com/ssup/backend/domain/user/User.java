package com.ssup.backend.domain.user;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.heart.comment.CommentHeart;
import com.ssup.backend.domain.heart.post.PostHeart;
import com.ssup.backend.domain.interest.UserInterest;
import com.ssup.backend.domain.user.language.UserLanguage;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@FilterDef(name = "activeUserFilter", parameters = @ParamDef(name = "status", type = String.class))
@Filter(name = "activeUserFilter", condition = "status = :status")
public class User extends BaseTimeEntity {

    //===== mapping =====

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<PostHeart> postHearts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<CommentHeart> commentHearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserLanguage> languages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserInterest> interests = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    //===== fields =====

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(length = 30)
    private String imageUrl;

    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String intro;

    @Column(length = 70)
    private String contact;

    //===== domian methods =====

    public void updateProfile(
            String nickname, String intro,
            int age, Gender gender, String contact
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.intro = intro;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateLocation(Location location) {
        this.location = location;
    }

    public void initProfile(String imageUrl, int age, Gender gender, String intro, String contact, Location location) {
        this.imageUrl = imageUrl;
        this.age = age;
        this.gender = gender;
        this.intro = intro;
        this.contact = contact;
        this.location = location;

        this.status = UserStatus.ACTIVE;
    }
}
