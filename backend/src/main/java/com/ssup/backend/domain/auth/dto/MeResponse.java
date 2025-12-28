package com.ssup.backend.domain.auth.dto;

import com.ssup.backend.domain.user.UserStatus;
import lombok.Getter;

@Getter
public class MeResponse {

    private Long id;
    private UserStatus status;

    public MeResponse(Long id, UserStatus status) {
        this.id = id;
        this.status = status;
    }
}
