package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileCreateRequest;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileResponse;
import com.ssup.backend.domain.user.profile.dto.UserProfileUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me")
@Tag(name = "User My Profile", description = "나의 프로필 API")
public class UserMeProfileController {

    private final UserProfileService userProfileService;
    private final AppUserProvider appUserProvider;

    @GetMapping("/profile")
    public UserMeProfileResponse findMyProfile() {
        return userProfileService.findMyProfile(appUserProvider.getUserId());
    }

    @PostMapping("/profile")
    public UserMeProfileResponse createMyProfile(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Valid @RequestPart("dto") UserMeProfileCreateRequest request
    ) {
        return userProfileService.createMyProfile(appUserProvider.getUserId(), image, request);
    }

    @PutMapping("/profile")
    public UserMeProfileResponse updateMyProfile(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @Valid @RequestPart("dto") UserProfileUpdateRequest request
    ) {
        return userProfileService.updateMyProfile(appUserProvider.getUserId(), image, request);
    }
}
