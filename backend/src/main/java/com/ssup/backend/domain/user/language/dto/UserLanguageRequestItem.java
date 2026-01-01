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

    @NotNull
    private Long languageId;

    @NotNull
    private LanguageType type;

    @NotNull
    private LanguageLevel level;
}