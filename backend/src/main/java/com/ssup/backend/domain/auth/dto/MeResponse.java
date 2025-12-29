package com.ssup.backend.domain.auth.dto;

import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeResponse {

    private Long id;
    private String nickname;
    private String imageUrl;
    private UserStatus status;

    public static MeResponse of(User user) {
        return MeResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .status(user.getStatus())
                .build();
    }
}
