package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.user.profile.dto.UserMeProfileCreateRequest;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileResponse;
import com.ssup.backend.domain.user.profile.dto.UserProfileUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public UserMeProfileResponse findMyProfile() {
        return userProfileService.findMyProfile(1L);
    }

    @PostMapping("/profile")
    public UserMeProfileResponse createMyProfile(@RequestPart(value = "image", required = false) MultipartFile image,
                                                 @RequestPart("dto") UserMeProfileCreateRequest request) {
        return userProfileService.createMyProfile(1L, request);
    }

    @PutMapping("/profile")
    public UserMeProfileResponse updateMyProfile(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("dto") UserProfileUpdateRequest request
    ) {
        return userProfileService.updateMyProfile(1L, image, request);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMyAccount() {
        userProfileService.deleteMyAccount(1L);
        return ResponseEntity.noContent().build();
    }
}
