package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.interest.dto.UserInterestViewResponse;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.match.MatchStatus;
import com.ssup.backend.domain.match.dto.MatchInfoResponse;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private Long id;

    private String nickname;

    private String imageUrl;

    private String intro;

    private int age;

    @Nullable
    @Schema(description = "매치된 사용자만 표시")
    private String contact;

    private UserLocationResponse location;

    private List<UserInterestViewResponse> interests;

    private MatchInfoResponse matchInfoResponse;

    public static UserProfileResponse of(User user, MatchInfoResponse matchInfoResponse) {
        Location siGunGu = user.getLocation();
        Location siDo = siGunGu.getParent();
        UserLocationResponse location = UserLocationResponse.builder()
                .siDoId(siDo.getId())
                .siDoName(siDo.getName())
                .siGunGuId(siGunGu.getId())
                .siGunGuName(siGunGu.getName())
                .build();

        //interests
        List<UserInterestViewResponse> interests =
                user.getInterests().stream()
                        .map(UserInterestViewResponse::of)
                        .toList();

        return UserProfileResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .intro(user.getIntro())
                .age(user.getAge())
                .contact(user.getContact())
                .location(location)
                .interests(interests)
                .matchInfoResponse(matchInfoResponse)
                .build();
    }
}

