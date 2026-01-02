package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.user.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeProfileCreateRequest {

    private String imageUrl;

    private int age;

    private Gender gender;

    private String intro;

    @NotBlank(message = "매칭 시 연락할 수 있는 연락처를 입력해주세요.")
    private String contact;

    @NotBlank(message = "활동 지역을 입력해주세요.")
    private UserLocationUpdateRequest location;

    private List<UserInterestRequestItem> interests;
}
