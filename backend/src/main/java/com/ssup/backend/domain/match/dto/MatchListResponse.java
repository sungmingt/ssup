package com.ssup.backend.domain.match.dto;

import com.ssup.backend.domain.match.Match;
import com.ssup.backend.domain.match.MatchStatus;
import com.ssup.backend.domain.match.MatchType;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchListResponse {

    private Long id;
    private Long partnerId;
    private String partnerName;
    private String partnerImageUrl;
    private MatchStatus matchStatus;
    private MatchType matchType;
    private LocalDateTime createdAt;

    public static List<MatchListResponse> of(List<Match> matches, Long currentUserId) {
        return matches.stream()
                .map(match -> {
                    boolean isSent = match.getRequester().getId().equals(currentUserId);
                    User partner = isSent ? match.getReceiver() : match.getRequester();

                    return new MatchListResponse(
                            match.getId(),
                            partner.getId(),
                            partner.getNickname(),
                            partner.getImageUrl(),
                            match.getStatus(),
                            isSent ? MatchType.REQUESTED : MatchType.RECEIVED,
                            match.getCreatedAt());
                })
                .toList();
    }
}