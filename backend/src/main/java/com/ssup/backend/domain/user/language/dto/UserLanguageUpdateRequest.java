package com.ssup.backend.domain.user.language.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLanguageUpdateRequest {

    @NotBlank(message = "사용/학습 언어를 입력 입력해주세요.")
    private List<UserLanguageRequestItem> languages;
}
