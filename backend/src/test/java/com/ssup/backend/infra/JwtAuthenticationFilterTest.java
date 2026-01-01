package com.ssup.backend.infra;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.auth.AuthController;
import com.ssup.backend.domain.auth.AuthService;
import com.ssup.backend.domain.auth.dto.MeResponse;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.support.TestSecurityConfig;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@ActiveProfiles("auth-test")
//component scan 하지않고, 얘네들만 bean 등록
@Import({TestSecurityConfig.class, JwtProvider.class, JwtCookieProvider.class})
class JwtAuthenticationFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtProvider jwtProvider;

    @MockBean
    AuthService authService;

    @MockBean
    AppUserProvider appUserProvider;

    @DisplayName("유효한 ATK Cookie가 존재할 경우, 인증에 성공한다.")
    @Test
    void validAccessTokenCookie_thenAuthenticated() throws Exception {
        //given
        Long userId = 1L;
        String token = jwtProvider.createAccessToken(userId);
        Cookie cookie = new Cookie("accessToken", token);
        MeResponse response = MeResponse.builder()
                .id(userId)
                .nickname("tester")
                .status(UserStatus.ACTIVE)
                .build();

        given(appUserProvider.getUserId()).willReturn(userId);
        given(authService.me(userId)).willReturn(response);

        //when, then
        mockMvc.perform(get("/api/auth/me").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @DisplayName("유효하지 않은 ATK Cookie인 경우, 인증에 실패한다.")
    @Test
    void invalidToken_thenUnauthorized() throws Exception {
        Cookie cookie = new Cookie("accessToken", "wrong.token");

        mockMvc.perform(get("/api/users/me/profile").cookie(cookie))
                .andExpect(status().isUnauthorized());
    }
}