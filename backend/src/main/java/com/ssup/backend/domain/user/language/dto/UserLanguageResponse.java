package com.ssup.backend.domain.user.language.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLanguageResponse {

    private List<UserLanguageResponseItem> usingLanguages;
    private List<UserLanguageResponseItem> learningLanguages;
}

