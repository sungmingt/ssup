package com.ssup.backend.domain.user.profile.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationUpdateRequest {

    @NotNull(message = "지역 정보를 입력해주세요.")
    @Min(value = 1, message = "올바른 지역을 선택해주세요.")
    private Long siGunGuId;
}