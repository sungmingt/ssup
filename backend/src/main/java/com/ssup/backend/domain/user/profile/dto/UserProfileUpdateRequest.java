package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.user.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    private String imageUrl;

    private String intro;

    @Positive(message = "올바른 나이를 입력해주세요.")
    private Integer age;

    private Gender gender;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String contact;

    private boolean removeImage;

    @Valid
    @NotNull(message = "활동 지역을 입력해주세요.")
    private UserLocationUpdateRequest userLocationUpdateRequest;

    private List<Long> interestIds;
}