package com.ssup.backend.domain.language;

import com.ssup.backend.domain.language.dto.LanguageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/languages")
@Tag(name = "Language", description = "언어 API")
public class LanguageController {


    private final LanguageService languageService;

    @Operation(summary = "글 목록 조회", description = "전체 글 목록 조회")
    @GetMapping
    public List<LanguageResponse> findList() {

        return languageService.findList();
    }
}
