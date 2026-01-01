package com.ssup.backend.domain.auth.dto;

import com.ssup.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenReissueResponse {

    private String code;
    private String message;

    public static TokenReissueResponse of(ErrorCode errorCode) {
        return new TokenReissueResponse(errorCode.name(), errorCode.getMessage());
    }
}
