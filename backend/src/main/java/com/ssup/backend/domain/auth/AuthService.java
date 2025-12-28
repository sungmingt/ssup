package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.LoginRequest;
import com.ssup.backend.domain.auth.dto.MeResponse;
import com.ssup.backend.domain.auth.dto.SignUpRequest;
import com.ssup.backend.domain.auth.dto.SignUpResponse;
import com.ssup.backend.domain.user.SocialType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import com.ssup.backend.infra.security.jwt.TokenStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ssup.backend.global.exception.ErrorCode.*;
import static com.ssup.backend.infra.security.jwt.JwtCookieProvider.COOKIE_HEADER;
import static com.ssup.backend.infra.security.jwt.TokenInfo.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final JwtCookieProvider cookieProvider;

    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new SsupException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new SsupException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        User user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password((request.getPassword())) //todo: 암호화
                .status(UserStatus.PENDING)
                .socialType(SocialType.NONE)
                .build();

        User savedUser = userRepository.save(user);
        return SignUpResponse.of(savedUser);
    }

    public MeResponse me(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(ErrorCode.USER_NOT_FOUND));

        return new MeResponse(user.getId(), user.getStatus());
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN)
                .orElseThrow(() -> new SsupException(REFRESH_TOKEN_NOT_FOUND));

        TokenStatus tokenStatus = jwtProvider.validateToken(refreshToken);

        //유효하지 않은 토큰
        if (tokenStatus == TokenStatus.INVALID) {
            cookieProvider.deleteAuthCookies(response);
            throw new SsupException(INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.getUserIdFromToken(refreshToken);

        //만료된 토큰
        if (tokenStatus == TokenStatus.EXPIRED) {
            refreshTokenRepository.deleteById(userId);
            cookieProvider.deleteAuthCookies(response);
            throw new SsupException(REFRESH_TOKEN_EXPIRED);
        }

        String savedRefresh = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new SsupException(REFRESH_TOKEN_NOT_FOUND));

        //올바른 토큰이지만, DB 일치 x
        if (!jwtProvider.checkRefreshTokenSameness(refreshToken, savedRefresh)) {
            //탈취 의심 -> 폐기 후 재로그인 유도
            refreshTokenRepository.deleteById(userId);
            cookieProvider.deleteAuthCookies(response);
            throw new SsupException(REFRESH_TOKEN_MISMATCH);
        }

        String newAccessToken = jwtProvider.createAccessToken(userId);
        ResponseCookie accessTokenCookie = cookieProvider.reissueAccessTokenCookie(newAccessToken);
        response.addHeader(COOKIE_HEADER, accessTokenCookie.toString());
    }

    public void login(LoginRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new SsupException(EMAIL_NOT_EXISTS));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new SsupException(PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(accessToken);
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(refreshToken);
        response.addHeader(COOKIE_HEADER, accessTokenCookie.toString());
        response.addHeader(COOKIE_HEADER, refreshTokenCookie.toString());
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieProvider.getTokenFromCookie(request, REFRESH_TOKEN).orElse(null);

        if (refreshToken != null) {
            Long userId = jwtProvider.parseClaims(refreshToken)
                    .get("userId", Long.class);

            refreshTokenRepository.deleteById(userId);
        }

        cookieProvider.deleteAuthCookies(response);
    }
}
