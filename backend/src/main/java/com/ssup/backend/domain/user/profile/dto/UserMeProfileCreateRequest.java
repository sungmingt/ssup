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
public class UserMeProfileCreateRequest {

    private String imageUrl;

    @Positive(message = "올바른 나이를 입력해주세요.")
    private int age;

    private Gender gender;

    private String intro;

    @NotBlank(message = "매칭 시 연락할 수 있는 연락처를 입력해주세요.")
    private String contact;

    @Valid //내부 객체안의 검증 로직을 실행하도록
    @NotNull(message = "활동 지역을 입력해주세요.")
    private UserLocationUpdateRequest location;

    private List<UserInterestRequestItem> interests;
}
