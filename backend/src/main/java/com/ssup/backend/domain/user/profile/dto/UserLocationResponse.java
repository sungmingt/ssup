package com.ssup.backend.domain.user.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLocationResponse {

    private long siDoId;
    private String siDoName;

    private long siGunGuId;
    private String siGunGuName;
}
