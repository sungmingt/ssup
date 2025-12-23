package com.ssup.backend.domain.user.profile.dto;

import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.user.SocialType;
import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static UserMeProfileResponse of(User user) {
        Location siGunGu = user.getLocation();
        Location siDo = siGunGu.getParent();
        UserLocationResponse location = UserLocationResponse.builder()
                .siDoId(siDo.getId())
                .siDoName(siDo.getName())
                .siGunGuId(siGunGu.getId())
                .siGunGuName(siGunGu.getName())
                .build();

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
                .build();
    }
}
