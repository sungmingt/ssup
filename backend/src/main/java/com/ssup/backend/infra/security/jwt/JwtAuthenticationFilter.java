package com.ssup.backend.infra.security.jwt;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.infra.security.SecurityPaths;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.ssup.backend.infra.security.jwt.TokenInfo.ACCESS_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtCookieProvider cookieProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String accessToken = cookieProvider.getTokenFromCookie(request, ACCESS_TOKEN).orElse(null);
        TokenStatus status = jwtProvider.validateToken(accessToken);

        if (status == TokenStatus.INVALID || status == TokenStatus.EXPIRED) {
            response.sendError(401);
            return;
        }

        Long userId = jwtProvider.getUserIdFromToken(accessToken);
        AppUser appUser = new AppUser(userId);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        appUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return Stream.of(
                        SecurityPaths.SWAGGER,
                        SecurityPaths.AUTH,
                        SecurityPaths.PUBLIC_GET,
                        SecurityPaths.OTHERS
                ).flatMap(Arrays::stream)
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
}