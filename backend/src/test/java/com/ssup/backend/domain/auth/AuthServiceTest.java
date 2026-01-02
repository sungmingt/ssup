package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.SignUpRequest;
import com.ssup.backend.domain.auth.dto.SignUpResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.fixture.user.UserFixture;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

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

    @DisplayName("일반 회원가입 - 성공")
    @Test
    void signUp_success() {
        //given
        SignUpRequest request = SignUpRequest.builder()
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
        SignUpResponse response = authService.signUp(request);

        //then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUp_emailDuplicate() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .password("1234")
                .build();

        given(userRepository.existsByEmail("test@gmail.com")).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());

        verify(userRepository, never()).save(any());
    }

    @DisplayName("회원가입 실패 - 닉네임 중복")
    @Test
    void signUp_nicknameDuplicate() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .password("1234")
                .build();

        given(userRepository.existsByEmail("test@gmail.com")).willReturn(false);
        given(userRepository.existsByNickname("tester")).willReturn(true);

        //when, then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.NICKNAME_ALREADY_EXISTS.getMessage());

        verify(userRepository, never()).save(any());
    }
}
