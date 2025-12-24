package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.user.Gender;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeProfileCreateRequest {

    private String imageUrl;

    private int age;

    private Gender gender;

    private String intro;

    @NotEmpty
    private String contact;

    @NotEmpty
    private UserLocationUpdateRequest location;
}
