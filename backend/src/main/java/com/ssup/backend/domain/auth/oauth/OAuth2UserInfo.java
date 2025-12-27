package com.ssup.backend.domain.auth.oauth;

import com.ssup.backend.domain.user.SocialType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserStatus;

public interface OAuth2UserInfo {

    String getName();
    String getEmail();
    SocialType getSocialType();
    String getSocialId();

    default User toEntity() {
        return User.builder()
                .nickname(getName())
                .email(getEmail())
                .socialType(getSocialType())
                .socialId(getSocialId())
                .status(UserStatus.PENDING)
                .build();
    }
}
