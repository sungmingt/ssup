package com.ssup.backend.domain.user.language.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @Valid
    @NotEmpty(message = "사용언어/학습언어를 선택해주세요.")
    private List<UserLanguageRequestItem> languages;
}
