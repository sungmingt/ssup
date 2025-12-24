package com.ssup.backend.domain.user.language;

import com.ssup.backend.domain.user.language.dto.UserLanguageResponse;
import com.ssup.backend.domain.user.language.dto.UserLanguageUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Language", description = "사용자 언어 설정 API")
public class UserLanguageController {

    private final UserLanguageService userLanguageService;

    @Operation(summary = "유저의 사용언어/학습언어 조회", description = "유저의 사용언어/학습언어 조회")
    @GetMapping("/{userId}/languages")
    public UserLanguageResponse findUserLanguage(@PathVariable("userId") Long userId) {

        return userLanguageService.findUserLanguages(userId);
    }

    @Operation(summary = "유저의 사용언어/학습언어 생성 및 수정", description = "유저의 사용언어/학습언어 생성 및 수정")
    @PutMapping("/me/languages")
    public void updateUserLanguage(@RequestBody UserLanguageUpdateRequest request) {

        userLanguageService.updateUserLanguages(1L, request);
    }
}
