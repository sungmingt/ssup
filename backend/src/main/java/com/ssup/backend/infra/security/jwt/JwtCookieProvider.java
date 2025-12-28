package com.ssup.backend.infra.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import static com.ssup.backend.infra.security.jwt.TokenInfo.*;

@Component
public class JwtCookieProvider {

    public static String COOKIE_HEADER = "Set-Cookie";

    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();
    }

    public ResponseCookie reissueAccessTokenCookie(String token) {
        return ResponseCookie.from(ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofMinutes(30))
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
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refresh = ResponseCookie.from(REFRESH_TOKEN, "")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", access.toString());
        response.addHeader("Set-Cookie", refresh.toString());
    }
}
