package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.domain.auth.CurrentUser;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileCreateRequest;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileResponse;
import com.ssup.backend.domain.user.profile.dto.UserProfileUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me")
@Tag(name = "User My Profile", description = "나의 프로필 API")
public class UserMeProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    public UserMeProfileResponse findMyProfile(@CurrentUser AppUser appUser) {
        return userProfileService.findMyProfile(appUser.getId());
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public UserMeProfileResponse createMyProfile(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("dto") UserMeProfileCreateRequest request,
            @CurrentUser AppUser appUser
            ) {

        return userProfileService.createMyProfile(appUser.getId(), image, request);
    }

    @PutMapping("/profile")
    public UserMeProfileResponse updateMyProfile(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("dto") UserProfileUpdateRequest request,
            @CurrentUser AppUser appUser
    ) {
        return userProfileService.updateMyProfile(appUser.getId(), image, request);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteMyAccount(@CurrentUser AppUser appUser) {
        userProfileService.deleteMyAccount(appUser.getId());
        return ResponseEntity.noContent().build();
    }
}
