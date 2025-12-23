package com.ssup.backend.domain.user.language.dto;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private List<UserLanguageRequestItem> languages;
}
