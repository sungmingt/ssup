package com.ssup.backend.domain.user;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.heart.comment.CommentHeart;
import com.ssup.backend.domain.heart.post.PostHeart;
import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.UserInterest;
import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.language.UserLanguage;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_social",
                        columnNames = {"social_type", "social_id"}
                )
        })
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
    private Set<UserLanguage> languages = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserInterest> interests = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    //===== fields =====

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(nullable = true, unique = true)
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

    public void initProfile(int age, Gender gender, String intro, String contact) {
        this.age = age;
        this.gender = gender;
        this.intro = intro;
        this.contact = contact;
    }

    public void addLanguage(Language language, LanguageLevel level, LanguageType type) {
        this.languages.add(new UserLanguage(this, language, level, type));
    }

    public void addInterest(Interest interest) {
        this.interests.add(new UserInterest(this, interest));
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }
}
