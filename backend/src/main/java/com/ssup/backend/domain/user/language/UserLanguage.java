package com.ssup.backend.domain.user.language;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "language_id", "type"}
                )
        }
)
public class UserLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageLevel level;

    @Enumerated(EnumType.STRING)
    private LanguageType type;

    public UserLanguage (User user, Language language, LanguageLevel level, LanguageType type) {
        this.user = user;
        this.language = language;
        this.level = level;
        this.type = type;
        user.getLanguages().add(this);
    }
}
