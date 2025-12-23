package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {

    private String nickname;
    private String imageUrl;
    private String intro;
    private Integer age;
    private Gender gender;
    private String contact;
    private boolean removeImage;

    private UserLocationUpdateRequest userLocationUpdateRequest;
}