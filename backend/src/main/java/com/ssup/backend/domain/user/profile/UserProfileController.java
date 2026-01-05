package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.user.profile.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Profile", description = "유저 프로필 API")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final AppUserProvider appUserProvider;

    @Operation(summary = "다른 유저의 프로필 조회", description = "다른 유저의 프로필 조회")
    @GetMapping("/{userId}/profile")
    public UserProfileResponse getUserProfile(@PathVariable("userId") Long userId) {
        return userProfileService.findUserProfile(appUserProvider.getUserId(), userId);
    }
}
