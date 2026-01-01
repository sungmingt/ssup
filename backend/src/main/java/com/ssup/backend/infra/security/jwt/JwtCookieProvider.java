package com.ssup.backend.infra.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import static com.ssup.backend.infra.security.jwt.TokenInfo.*;

@Component
public class JwtCookieProvider {

    @Value("${app.cookie-domain:#{null}}")
    private String cookieDomain;

    public static final String COOKIE_HEADER = "Set-Cookie";

    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .domain(cookieDomain)
                .maxAge(Duration.ofMillis(REFRESH_TOKEN_TTL_MILLISECONDS)) //만료시 재발급 처리를 위해 쿠키는 유지해야한다.
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .domain(cookieDomain)
                .maxAge(Duration.ofMillis(REFRESH_TOKEN_TTL_MILLISECONDS))
                .build();
    }

    public ResponseCookie reissueAccessTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .domain(cookieDomain)
                .maxAge(Duration.ofMillis(ACCESS_TOKEN_TTL_MILLISECONDS))
                .build();
    }

    public Optional<String> getTokenFromCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst();
    }

    public void deleteAuthCookies(HttpServletResponse response) {
        ResponseCookie access = ResponseCookie.from(ACCESS_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(0)
                .path("/")
                .domain(cookieDomain)
                .build();

        ResponseCookie refresh = ResponseCookie.from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(0)
                .path("/")
                .domain(cookieDomain)
                .build();

        response.addHeader("Set-Cookie", access.toString());
        response.addHeader("Set-Cookie", refresh.toString());
    }
}
