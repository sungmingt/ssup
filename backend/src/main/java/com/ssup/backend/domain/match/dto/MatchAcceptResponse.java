package com.ssup.backend.domain.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchAcceptResponse {

    private Long id;
    private String partnerContact;
    private String message;

    public static MatchAcceptResponse of(Long id, String partnerContact) {
        return MatchAcceptResponse.builder().
                id(id)
                .partnerContact(partnerContact)
                .message("상대방과 매칭되었습니다! 연락처를 통해 먼저 인사를 건네보세요!")
                .build();
    }
}
