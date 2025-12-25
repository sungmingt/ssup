package com.ssup.backend.domain.interest;

import com.ssup.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "interest_id"}))
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interest_id")
    private Interest interest;

    private Integer priority; //관심도/순서

    public UserInterest(User user, Interest interest) {
        this.user = user;
        this.interest = interest;
    }
}

