package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.interest.dto.UserInterestResponse;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.user.SocialType;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMeProfileResponse {

    private Long id;

    private String email;

    private String nickname;

    private String imageUrl;

    private String intro;

    private SocialType socialType;

    private int age;

    private String contact;

    private UserLocationResponse location;

    private List<UserInterestResponse> interests;

    public static UserMeProfileResponse of(User user) {
        //location
        Location siGunGu = user.getLocation();
        Location siDo = siGunGu.getParent();
        UserLocationResponse location = UserLocationResponse.builder()
                .siDoId(siDo.getId())
                .siDoName(siDo.getName())
                .siGunGuId(siGunGu.getId())
                .siGunGuName(siGunGu.getName())
                .build();

        //interests
        List<UserInterestResponse> interests =
                user.getInterests().stream()
                        .map(UserInterestResponse::of)
                        .toList();

        return UserMeProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .intro(user.getIntro())
                .socialType(user.getSocialType())
                .age(user.getAge())
                .contact(user.getContact())
                .location(location)
                .interests(interests)
                .build();
    }
}
