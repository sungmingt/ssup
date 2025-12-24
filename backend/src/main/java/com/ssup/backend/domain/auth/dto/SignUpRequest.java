package com.ssup.backend.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SignUpRequest { //일반 로그인

    @Email
    @NotEmpty
    private String nickname;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
