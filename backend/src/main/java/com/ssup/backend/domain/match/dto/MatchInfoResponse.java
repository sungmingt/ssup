package com.ssup.backend.domain.match.dto;

import com.ssup.backend.domain.match.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchInfoResponse {

    private MatchStatus matchStatus;
    private boolean amIRequester;
}
