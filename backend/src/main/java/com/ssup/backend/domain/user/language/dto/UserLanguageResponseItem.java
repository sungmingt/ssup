package com.ssup.backend.domain.user.language.dto;

import com.ssup.backend.domain.language.LanguageLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLanguageResponseItem {

    private Long id;

    private String code;

    @Schema(description = "언어 이름", example = "English")
    private String name;

    @Schema(description = "언어 레벨", example = "INTERMEDIATE")
    private LanguageLevel level;
}