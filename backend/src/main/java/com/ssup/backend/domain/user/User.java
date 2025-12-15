package com.ssup.backend.domain.user;

import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import com.ssup.backend.domain.language.UserLanguage;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @OneToMany(mappedBy = "author")
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    //    @OneToMany(mappedBy = "user")
//    private List<Comment> comments = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<Heart> hearts = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(length = 10, nullable = false)
    private String nickname;

//    @Column(nullable = false, unique = true)
    private String email;

    private String password;

//    @Enumerated(EnumType.STRING)
//    private SocialType socialType;

    private String socialId;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id")
//    private Location location;

    @Column(length = 30)
    private String imageUrl;

    private String intro;

    @Column(length = 50)
    private String contact;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<UserLanguage> languages = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
