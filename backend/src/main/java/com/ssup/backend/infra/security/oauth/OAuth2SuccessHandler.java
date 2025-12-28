package com.ssup.backend.infra.security.oauth;

import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ssup.backend.infra.security.jwt.JwtCookieProvider.COOKIE_HEADER;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${front.origin}")
    private String frontOrigin;

    private final JwtProvider jwtProvider;
    private final JwtCookieProvider jwtCookieProvider;

    //여기서는 토큰/쿠키 생성 후 리다이렉트만 담당한다. (유저 생성은 CustomOAuth2UserService에서 처리)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Long userId = (Long) principal.getAttributes().get("userId");
        UserStatus userStatus = (UserStatus) principal.getAttributes().get("userStatus");

        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        ResponseCookie accessCookie = jwtCookieProvider.createAccessTokenCookie(accessToken);
        ResponseCookie refreshCookie = jwtCookieProvider.createRefreshTokenCookie(refreshToken);
        response.addHeader(COOKIE_HEADER, accessCookie.toString());
        response.addHeader(COOKIE_HEADER, refreshCookie.toString());

        if (userStatus == (UserStatus.PENDING)) {
            response.sendRedirect(frontOrigin + "/signup/additional");
        } else if (userStatus.equals(UserStatus.ACTIVE)) {
            response.sendRedirect(frontOrigin);
        }
    }
}
