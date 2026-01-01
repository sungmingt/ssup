package com.ssup.backend.domain.user.profile.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationUpdateRequest {

    @NotNull
    private Long siGunGuId;
}