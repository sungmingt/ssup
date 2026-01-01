package com.ssup.backend.domain.language;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "code")})
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //내부 식별용
    @Column(length = 20, nullable = false)
    private String code; // EN, KO, JA ...

    //사용자에게 보여줄 이름
    @Column(length = 20, nullable = false)
    private String name;
}
