package com.ssup.backend.domain.user.language.dto;

import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLanguageRequestItem {

    @NotNull(message = "사용/학습 언어를 입력 입력해주세요.")
    private Long languageId;

    @NotNull
    private LanguageType type;

//    @NotNull
    private LanguageLevel level;
}