package com.ssup.backend.domain.match;

import com.ssup.backend.domain.user.User;
import com.ssup.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "matches")
public class Match extends BaseTimeEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    //===== methods =====

    public void accept() {
        this.status = MatchStatus.ACCEPTED;
    }

    public void reject() {
        this.status = MatchStatus.REJECTED;
    }
}