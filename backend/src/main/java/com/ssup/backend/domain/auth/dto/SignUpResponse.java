package com.ssup.backend.domain.auth.dto;

import com.ssup.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpResponse {

    private Long id;

    private String nickname;

    private String email;

    public static SignUpResponse of(User user) {
        return new SignUpResponse(user.getId(), user.getNickname(), user.getEmail());
    }
}
