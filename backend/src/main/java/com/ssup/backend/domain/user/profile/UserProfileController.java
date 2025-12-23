package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.user.profile.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Profile", description = "사용자 프로필 API")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{userId}/profile")
    public UserProfileResponse getUserProfile(@PathVariable("userId") Long userId) {
        return userProfileService.findUserProfile(userId);
    }
}
