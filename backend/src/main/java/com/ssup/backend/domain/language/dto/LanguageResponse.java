package com.ssup.backend.domain.language.dto;

import com.ssup.backend.domain.language.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LanguageResponse {

    private Long id;
    private String code;
    private String name;

    public static LanguageResponse of(Language language) {
        return new LanguageResponse(
                language.getId(),
                language.getCode(),
                language.getName()
        );
    }
}