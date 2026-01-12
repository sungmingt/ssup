package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.LoginRequest;
import com.ssup.backend.domain.auth.dto.SignUpRequest;
import com.ssup.backend.domain.auth.dto.SignUpResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import com.ssup.backend.infra.security.jwt.TokenStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.ssup.backend.infra.security.jwt.TokenInfo.REFRESH_TOKEN;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private JwtCookieProvider cookieProvider;
    @Mock
    private ImageStorage imageStorage;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @DisplayName("일반 회원가입 - 성공")
    @Test
    void signUp_success() {
        //given
        SignUpRequest dto = SignUpRequest.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .password("1234")
                .build();

        given(userRepository.existsByEmail("test@gmail.com")).willReturn(false);
        given(userRepository.existsByNickname("tester")).willReturn(false);

        User savedUser = User.builder()
                .id(1L)
                .nickname("tester")
                .email("test@gmail.com")
                .build();

        given(userRepository.save(any(User.class))).willReturn(savedUser);

        //when
        SignUpResponse res = authService.signUp(dto);

        //then
        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUp_fail_emailDuplicate() {
        //given
        SignUpRequest dto = SignUpRequest.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .password("1234")
                .build();

        given(userRepository.existsByEmail("test@gmail.com")).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.signUp(dto))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());

        verify(userRepository, never()).save(any());
    }

    @DisplayName("회원가입 실패 - 닉네임 중복")
    @Test
    void signUp_fail_nicknameDuplicate() {
        //given
        SignUpRequest dto = SignUpRequest.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .password("1234")
                .build();

        given(userRepository.existsByEmail("test@gmail.com")).willReturn(false);
        given(userRepository.existsByNickname("tester")).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.signUp(dto))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.NICKNAME_ALREADY_EXISTS.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_passwordNotMatch() {
        //given
        User user = User.builder()
                .id(1L)
                .email("a@test.com")
                .password(PasswordEncryptor.encode("correct"))
                .status(UserStatus.ACTIVE)
                .build();

        given(userRepository.findByEmail("a@test.com")).willReturn(Optional.of(user));

        LoginRequest dto = new LoginRequest("a@test.com","wrong");

        //when, then
        assertThatThrownBy(() -> authService.login(dto, response))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
    }

    @Test
    @DisplayName("reissue 실패 - INVALID refresh token")
    void reissue_fail_invalidRefreshToken() {
        //given
        given(cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN))
                .willReturn(Optional.of("rtk"));
        given(jwtProvider.validateToken("rtk"))
                .willReturn(TokenStatus.INVALID);

        //when
        assertThatThrownBy(() -> authService.reissue(request,response))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());

        //then
        verify(cookieProvider).deleteAuthCookies(response);
    }

    @Test
    @DisplayName("reissue 실패 - 만료된 refresh token")
    void reissue_fail_tokenExpired() {
        //given
        String sessionId = UUID.randomUUID().toString();
        given(cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN))
                .willReturn(Optional.of("rtk"));
        given(jwtProvider.validateToken("rtk"))
                .willReturn(TokenStatus.EXPIRED);
        given(jwtProvider.getUserIdFromToken("rtk"))
                .willReturn(1L);
        given(jwtProvider.getSessionIdFromToken("rtk"))
                .willReturn(sessionId);


        //when
        assertThatThrownBy(() -> authService.reissue(request,response))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());

        //then
        verify(refreshTokenRepository).deleteById(1L, sessionId);
    }

    @Test
    @DisplayName("reissue 실패 - Redis RTK 불일치")
    void reissue_fail_mismatch() {
        //given
        given(cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN))
                .willReturn(Optional.of("rtk"));
        given(jwtProvider.validateToken("rtk"))
                .willReturn(TokenStatus.VALID);
        given(jwtProvider.getUserIdFromToken("rtk"))
                .willReturn(1L);
        given(jwtProvider.getSessionIdFromToken("rtk"))
                .willReturn("sessionId");
        given(refreshTokenRepository.findByUserId(1L, "sessionId"))
                .willReturn(Optional.of("other"));
        given(jwtProvider.checkRefreshTokenSameness("rtk","other"))
                .willReturn(false);

        //when, then
        assertThatThrownBy(() -> authService.reissue(request,response))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("로그아웃 - 성공 (refresh token 존재)")
    void logout_withToken() {
        //given
        given(cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN))
                .willReturn(Optional.of("rtk"));
        given(jwtProvider.getUserIdFromToken("rtk")).willReturn(1L);
        given(jwtProvider.getSessionIdFromToken("rtk")).willReturn("sessionId");

        //when
        authService.logout(request,response);

        //then
        verify(refreshTokenRepository).deleteById(1L, "sessionId");
        verify(cookieProvider).deleteAuthCookies(response);
    }

    @Test
    @DisplayName("회원 탈퇴 시 프로필 이미지도 함께 삭제된다")
    void quit_withImage() {
        //given
        User user = User.builder()
                .id(1L)
                .imageUrl("s3://test.png")
                .status(UserStatus.ACTIVE)
                .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN))
                .willReturn(Optional.of("rtk"));
        given(jwtProvider.getUserIdFromToken("rtk")).willReturn(1L);

        //when
        authService.quit(1L, request, response);

        //then
        verify(imageStorage).deleteByUrl("s3://test.png");
    }
}
