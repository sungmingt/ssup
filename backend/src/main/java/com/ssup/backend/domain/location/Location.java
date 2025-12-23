package com.ssup.backend.domain.location;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    //level: 1 = 시/도, 2 = 군/구
    @Column(nullable = false)
    private Integer level;

    //self-referencing parent_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Location parent;
}
